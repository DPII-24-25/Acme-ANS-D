
package acme.features.flight_crew_member.flight_assignment;

import java.util.Collection;
import java.util.Date;

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
public class FlightCrewMemberFlightAssignmentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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
		this.repository.delete(object);
	}

	@Override
	public void validate(final FlightAssignment object) {
		assert object != null;

		// Validación 1: Asegurarse de que está en modo borrador
		super.state(object.isDraftMode(), "*", "acme.validation.flightAssignment.mustBeDraftToDelete");
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
