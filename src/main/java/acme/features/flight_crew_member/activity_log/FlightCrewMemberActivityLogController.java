
package acme.features.flight_crew_member.activity_log;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.activityLog.ActivityLog;
import acme.realms.FlightCrewMember;

@GuiController
public class FlightCrewMemberActivityLogController extends AbstractGuiController<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogListService	listService;

	@Autowired
	private FlightCrewMemberActivityLogShowService	showService;


	/*
	 * @Autowired
	 * private FlightCrewMemberActivityLogCreateService createService;
	 * 
	 * @Autowired
	 * private FlightCrewMemberActivityLogUpdateService updateService;
	 * 
	 */
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		//	super.addBasicCommand("create", this.createService);
		//super.addBasicCommand("update", this.updateService);
	}

}
