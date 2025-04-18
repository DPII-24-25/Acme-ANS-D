
package acme.features.flight_crew_member.activity_log;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean owner;
		int masterId;
		int activeFlightAssignmentId;
		FlightAssignment flightAssignment;

		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		activeFlightAssignmentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		owner = flightAssignment.getFlightCrewMember().getId() == activeFlightAssignmentId;

		super.getResponse().setAuthorised(status && owner && !flightAssignment.isDraftMode());
	}

	@Override
	public void load() {
		Collection<ActivityLog> activityLogs;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		activityLogs = this.repository.findActivityLogsByFlightAssignmentId(masterId);

		super.getBuffer().addData(activityLogs);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel");

		if (activityLog.isDraftMode()) {
			final Locale local = super.getRequest().getLocale();

			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");
		} else
			dataset.put("draftMode", "No");
		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<ActivityLog> activityLog) {

		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);
	}

}
