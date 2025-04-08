
package acme.features.flight_crew_member.activity_log;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int trackingLogId;
		FlightAssignment flightAssignment;
		ActivityLog al;

		trackingLogId = super.getRequest().getData("id", int.class);
		al = this.repository.findActivityLogById(trackingLogId);
		flightAssignment = al.getFlightAssignment();

		int activeFlightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean flightCrewMemberOwnsAl = al.getFlightAssignment().getFlightCrewMember().getId() == activeFlightCrewMemberId;
		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		super.getResponse().setAuthorised(status && flightAssignment != null && flightCrewMemberOwnsAl && !flightAssignment.isDraftMode());
	}

	@Override
	public void load() {
		ActivityLog object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findActivityLogById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {

		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("masterId", activityLog.getFlightAssignment().getId());

		super.getResponse().addData(dataset);
	}

}
