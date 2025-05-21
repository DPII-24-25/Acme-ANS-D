
package acme.features.flight_crew_member.activity_log;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int masterId;
		FlightAssignment flightAssignment;

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);

		int activeFlightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean flightCrewMemberOwnsAl = flightAssignment.getFlightCrewMember().getId() == activeFlightCrewMemberId;
		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		super.getResponse().setAuthorised(status && flightAssignment != null && flightCrewMemberOwnsAl && !flightAssignment.isDraftMode());
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		FlightAssignment flightAssignment;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		activityLog = new ActivityLog();
		activityLog.setDraftMode(true);
		activityLog.setFlightAssignment(flightAssignment);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
		final Date cMoment = MomentHelper.getCurrentMoment();
		activityLog.setRegistrationMoment(cMoment);
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		assert activityLog != null;

		FlightAssignment assignment = activityLog.getFlightAssignment();

		// Validar que el assignment esté confirmado
		boolean isConfirmed = assignment.getStatus().name().equals("CONFIRMED");
		super.state(isConfirmed, "*", "acme.validation.activitylog.assignmentNotConfirmed");

		// Validar que el leg esté completado (fecha de llegada en el pasado)
		boolean legCompleted = MomentHelper.isPast(assignment.getLeg().getScheduleArrival());
		super.state(legCompleted, "*", "acme.validation.activitylog.legNotCompleted");

		// Validar que registrationMoment sea >= scheduleArrival
		boolean registrationAfterArrival = MomentHelper.isAfterOrEqual(activityLog.getRegistrationMoment(), assignment.getLeg().getScheduleArrival());
		super.state(registrationAfterArrival, "registrationMoment", "acme.validation.activitylog.registrationmoment.tooEarly");
	}

	@Override
	public void unbind(final ActivityLog activityLog) {

		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("masterId", activityLog.getFlightAssignment().getId());

		super.getResponse().addData(dataset);
	}

}
