
package acme.features.flight_crew_member.flight_assignment;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Leg;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.FlightAssignmentStatus;
import acme.entities.flightAssignment.FlightCrewDuty;
import acme.realms.FlightCrewAvailability;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean isFlightCrewMember = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		FlightCrewMember member = this.repository.findFlightCrewMemberById(flightCrewMemberId);
		boolean isAvailable = member.getAvailabilityStatus() == FlightCrewAvailability.AVAILABLE;

		if (!isFlightCrewMember)
			super.getResponse().setAuthorised(false);
		else if (!isAvailable) {
			super.getResponse().setAuthorised(false);
			super.getResponse().addGlobal("errorMessage", "No puede crear asignaciones mientras su estado sea '" + member.getAvailabilityStatus().name().replace("_", " ").toLowerCase() + "'.");
		} else
			super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment = new FlightAssignment();
		FlightCrewMember flightCrewMember;
		flightCrewMember = this.repository.findFlightCrewMemberById(super.getRequest().getPrincipal().getActiveRealm().getId());
		flightAssignment.setDraftMode(true);
		flightAssignment.setStatus(FlightAssignmentStatus.PENDING);
		flightAssignment.setFlightCrewMember(flightCrewMember);
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment object) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		super.bindObject(object, "duty", "remarks");
		final Date cMoment = MomentHelper.getCurrentMoment();
		object.setLastUpdate(cMoment);
		object.setLeg(leg);
	}

	@Override
	public void perform(final FlightAssignment object) {
		this.repository.save(object);
	}

	@Override
	public void validate(final FlightAssignment object) {
		assert object != null;

		boolean isAvailable = object.getFlightCrewMember().getAvailabilityStatus() == FlightCrewAvailability.AVAILABLE;
		super.state(isAvailable, "*", "acme.validation.flightAssignment.memberNotAvailable");

		if (object.getLeg() != null) {
			boolean isFutureLeg = !MomentHelper.isPast(object.getLeg().getScheduleArrival());
			super.state(isFutureLeg, "leg", "acme.validation.flightAssignment.legInPast");

			boolean legIsPublished = !object.getLeg().isDraftMode();
			super.state(legIsPublished, "leg", "acme.validation.flightAssignment.legNotPublished");

			Collection<Leg> allowedLegs = this.repository.findLegsAfterCurrentDateByAirlineId(object.getFlightCrewMember().getAirline().getId(), MomentHelper.getCurrentMoment());
			super.state(allowedLegs.contains(object.getLeg()), "leg", "acme.validation.flightAssignment.legNotInAirline");
		}

		if (object.getLeg() != null) {
			Collection<FlightAssignment> existingAssignments = this.repository.findFlightAssignmentByFlightCrewMemberId(object.getFlightCrewMember().getId());
			for (FlightAssignment fa : existingAssignments) {
				if (fa.getId() != object.getId() && !fa.getLeg().isDraftMode()) {
					boolean overlaps = MomentHelper.isInRange(object.getLeg().getScheduleDeparture(), fa.getLeg().getScheduleDeparture(), fa.getLeg().getScheduleArrival())
						|| MomentHelper.isInRange(object.getLeg().getScheduleArrival(), fa.getLeg().getScheduleDeparture(), fa.getLeg().getScheduleArrival());
					super.state(!overlaps, "*", "acme.validation.flightAssignment.overlappingLegs");
					if (overlaps)
						break;
				}
				if (fa.getLeg().equals(object.getLeg()) && fa.getFlightCrewMember().equals(object.getFlightCrewMember()) && fa.getId() != object.getId()) {
					super.state(false, "*", "acme.validation.flightAssignment.memberDuplicatedOnLeg");
					break;
				}
			}
		}

		if (object.getLeg() != null && object.getDuty() != null) {
			List<FlightAssignment> assignmentsByLeg = this.repository.findFlightAssignmentByLegId(object.getLeg().getId());

			boolean hasPilot = assignmentsByLeg.stream().anyMatch(fa -> fa.getDuty() == FlightCrewDuty.PILOT && fa.getId() != object.getId());
			boolean hasCoPilot = assignmentsByLeg.stream().anyMatch(fa -> fa.getDuty() == FlightCrewDuty.CO_PILOT && fa.getId() != object.getId());

			super.state(!(hasPilot && object.getDuty() == FlightCrewDuty.PILOT), "duty", "acme.validation.flightAssignment.onlyOnePilot");
			super.state(!(hasCoPilot && object.getDuty() == FlightCrewDuty.CO_PILOT), "duty", "acme.validation.flightAssignment.onlyOneCoPilot");
		}
	}

	@Override
	public void unbind(final FlightAssignment object) {
		SelectChoices choicesLegs;
		SelectChoices choicesDuty;
		Collection<Leg> legs;
		Dataset dataset;
		final Date cMoment = MomentHelper.getCurrentMoment();
		legs = this.repository.findLegsAfterCurrentDateByAirlineId(object.getFlightCrewMember().getAirline().getId(), cMoment);
		choicesLegs = SelectChoices.from(legs, "flightNumber", object.getLeg());
		choicesDuty = SelectChoices.from(FlightCrewDuty.class, object.getDuty());

		dataset = super.unbindObject(object, "duty", "lastUpdate", "remarks", "draftMode");
		dataset.put("leg", choicesLegs.getSelected().getKey());
		dataset.put("legs", choicesLegs);
		dataset.put("duties", choicesDuty);

		super.getResponse().addData(dataset);
	}
}
