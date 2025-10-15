
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
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {

		int masterId = this.getRequest().getData("id", int.class);
		boolean auth = this.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		int activeflightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(masterId);
		boolean owner = activeflightCrewMemberId == flightAssignment.getFlightCrewMember().getId();
		super.getResponse().setAuthorised(auth && owner && flightAssignment.isDraftMode());
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int masterId = this.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment object) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		super.bindObject(object, "duty", "status", "remarks");
		final Date cMoment = MomentHelper.getCurrentMoment();
		object.setLastUpdate(cMoment);
		object.setLeg(leg);
	}
	@Override
	public void perform(final FlightAssignment object) {
		assert object != null;
		object.setDraftMode(false);
		this.repository.save(object);
	}
	@Override
	public void validate(final FlightAssignment object) {
		assert object != null;

		// 1. Estado debe ser CONFIRMED
		boolean isPending = object.getStatus() == FlightAssignmentStatus.PENDING;
		super.state(!isPending, "status", "acme.validation.flightAssignment.mustNotBePending");

		// 2. Miembro debe estar disponible
		boolean isAvailable = object.getFlightCrewMember().getAvailabilityStatus().equals(FlightCrewAvailability.AVAILABLE);
		super.state(isAvailable, "flightCrewMember", "acme.validation.flightAssignment.memberNotAvailable");

		// 3. lastUpdate en el pasado
		boolean isLastUpdateInPast = !MomentHelper.isFuture(object.getLastUpdate());
		super.state(isLastUpdateInPast, "lastUpdate", "acme.validation.flightAssignment.lastUpdate.past");

		// 4. Leg debe estar en el futuro
		if (object.getLeg() != null) {
			boolean isFutureLeg = !MomentHelper.isPast(object.getLeg().getScheduleArrival());
			super.state(isFutureLeg, "leg", "acme.validation.flightAssignment.legInPast");

			// 5. Leg debe estar publicado
			boolean legIsPublished = !object.getLeg().isDraftMode();
			super.state(legIsPublished, "leg", "acme.validation.flightAssignment.legNotPublished");
		}

		// 6. Verificar solapamiento de horarios
		if (object.getLeg() != null) {
			Collection<FlightAssignment> existingAssignments = this.repository.findFlightAssignmentByFlightCrewMemberId(object.getFlightCrewMember().getId());
			for (FlightAssignment fa : existingAssignments)
				if (fa.getId() != object.getId() && !fa.getLeg().isDraftMode()) {
					boolean overlaps = MomentHelper.isInRange(object.getLeg().getScheduleDeparture(), fa.getLeg().getScheduleDeparture(), fa.getLeg().getScheduleArrival())
						|| MomentHelper.isInRange(object.getLeg().getScheduleArrival(), fa.getLeg().getScheduleDeparture(), fa.getLeg().getScheduleArrival());

					super.state(!overlaps, "*", "acme.validation.flightAssignment.overlappingLegs");

					if (overlaps)
						break;
				}
		}

		// 7. Un solo piloto y copiloto por leg
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
		SelectChoices choicesStatus;
		Collection<Leg> legs;
		Dataset dataset;

		final Date cMoment = MomentHelper.getCurrentMoment();
		legs = this.repository.findLegsAfterCurrentDateByAirlineId(object.getFlightCrewMember().getAirline().getId(), cMoment);

		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember member = this.repository.findFlightCrewMemberById(flightCrewMemberId);
		boolean available = member.getAvailabilityStatus() == FlightCrewAvailability.AVAILABLE;

		choicesLegs = SelectChoices.from(legs, "flightNumber", object.getLeg());
		choicesDuty = SelectChoices.from(FlightCrewDuty.class, object.getDuty());
		choicesStatus = SelectChoices.from(FlightAssignmentStatus.class, object.getStatus());

		dataset = super.unbindObject(object, "duty", "lastUpdate", "status", "remarks", "draftMode");
		dataset.put("leg", choicesLegs.getSelected().getKey());
		dataset.put("legs", choicesLegs);
		dataset.put("duties", choicesDuty);
		dataset.put("statuts", choicesStatus);
		dataset.put("estado", available);

		super.getResponse().addData(dataset);
	}
}
