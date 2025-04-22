
package acme.features.flight_crew_member.activity_log;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


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
		boolean assistanceAgentOwnsClaim = flightAssignment.getFlightCrewMember().getId() == activeFlightCrewMemberId;
		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		super.getResponse().setAuthorised(status && flightAssignment != null && assistanceAgentOwnsClaim && !flightAssignment.isDraftMode() && activityLog.isDraftMode());
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
	public void bind(final ActivityLog object) {
		int activityLogId;
		FlightAssignment flightAssignment;
		ActivityLog activityLog;

		activityLogId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(activityLogId);
		flightAssignment = activityLog.getFlightAssignment();

		super.bindObject(object, "typeOfIncident", "description", "severityLevel");
		object.setFlightAssignment(flightAssignment);
	}

	@Override
	public void perform(final ActivityLog object) {
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void validate(final ActivityLog object) {
		assert object != null;
	}

	@Override
	public void unbind(final ActivityLog object) {
		Dataset dataset;

		dataset = super.unbindObject(object, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("masterId", object.getFlightAssignment().getId());

		super.getResponse().addData(dataset);
	}

}
