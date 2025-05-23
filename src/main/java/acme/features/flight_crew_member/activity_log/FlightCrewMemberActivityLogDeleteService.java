
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
		int activityLogId = super.getRequest().getData("id", int.class);

		ActivityLog activityLog = this.repository.findActivityLogById(activityLogId);
		FlightAssignment flightAssignment = activityLog != null ? activityLog.getFlightAssignment() : null;

		boolean userIsCrew = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		int currentCrewId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean isOwner = flightAssignment != null && flightAssignment.getFlightCrewMember().getId() == currentCrewId;

		boolean canDelete = activityLog != null && isOwner && !flightAssignment.isDraftMode() && activityLog.isDraftMode();

		super.getResponse().setAuthorised(userIsCrew && canDelete);
	}

	@Override
	public void load() {
		int activityLogId = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findActivityLogById(activityLogId);
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.delete(activityLog);
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		assert activityLog != null;
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");

		dataset.put("masterId", activityLog.getFlightAssignment().getId());
		super.getResponse().addData(dataset);
	}
}
