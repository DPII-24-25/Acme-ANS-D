
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
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		FlightCrewMember member = this.repository.findFlightCrewMemberById(flightCrewMemberId);
		boolean available = member.getAvailabilityStatus() == FlightCrewAvailability.AVAILABLE;
		super.getResponse().setAuthorised(status && available);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment = new FlightAssignment();
		FlightCrewMember flightCrewMember;
		flightCrewMember = this.repository.findFlightCrewMemberById(super.getRequest().getPrincipal().getActiveRealm().getId());
		flightAssignment.setDraftMode(true);
		flightAssignment.setFlightCrewMember(flightCrewMember);
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
		this.repository.save(object);
	}

	@Override
	public void validate(final FlightAssignment object) {
		//if (!super.getBuffer().getErrors().hasErrors("")) {
		//FlightCrewMember flightCrewMember = object.getFlightCrewMember();

		// Validar que no exista otro flightAssignment con el mismo flighCrewMember
		// Validar que no exista otro flightAssignment con otro pilot, copilot, etc.
		// Validar que el member no tenga asignado otro leg con las mimas horas.
		// Verificar que los leg sean solo los que se permiten.
		// Verificar que el member este en AVAILABLE.
		// 
		assert object != null;
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
		choicesLegs = SelectChoices.from(legs, "flightNumber", object.getLeg());
		choicesDuty = SelectChoices.from(FlightCrewDuty.class, object.getDuty());
		choicesStatus = SelectChoices.from(FlightAssignmentStatus.class, object.getStatus());

		dataset = super.unbindObject(object, "duty", "lastUpdate", "status", "remarks", "draftMode");
		dataset.put("leg", choicesLegs.getSelected().getKey());
		dataset.put("legs", choicesLegs);
		dataset.put("duties", choicesDuty);
		dataset.put("statutes", choicesStatus);

		super.getResponse().addData(dataset);
	}

}
