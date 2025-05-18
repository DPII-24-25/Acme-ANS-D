
package acme.features.flight_crew_member.activity_log;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;
import acme.realms.FlightCrewMemberRepository;

@GuiService
public class FlightCrewMemberActivityLogDeleteService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int activityLogId;
		FlightAssignment flightAssignment;
		ActivityLog activityLog;

		activityLogId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(activityLogId);
		flightAssignment = activityLog.getFlightAssignment();

		int activeFlightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean FlightCrewMemberOwnsflightAssignment = flightAssignment.getFlightCrewMember().getId() == activeFlightCrewMemberId;
		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		super.getResponse().setAuthorised(status && flightAssignment != null && FlightCrewMemberOwnsflightAssignment && !flightAssignment.isDraftMode() && activityLog.isDraftMode());
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int activityLogId;

		activityLogId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(activityLogId);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void perform(final ActivityLog object) {
		this.repository.delete(object);
	}

	@Override
	public void validate(final ActivityLog object) {
		assert object != null;
	}

	@Override
	public void unbind(final ActivityLog activityLog) {

		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("masterId", activityLog.getFlightAssignment().getId());

		super.getResponse().addData(dataset);
	}

}
