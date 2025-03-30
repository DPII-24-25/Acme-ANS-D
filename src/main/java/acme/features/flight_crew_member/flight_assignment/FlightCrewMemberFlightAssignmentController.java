
package acme.features.flight_crew_member.flight_assignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiController
public class FlightCrewMemberFlightAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentListService		listService;

	@Autowired
	private FlightCrewMemberFlightAssignmentShowService		showService;

	@Autowired
	private FlightCrewMemberFlightAssignmentCreateService	createService;

	@Autowired
	private FlightCrewMemberFlightAssignmentUpdateService	updateService;

	@Autowired
	private FlightCrewMemberFlightAssignmentDeleteService	deleteService;

	@Autowired
	private FlightCrewMemberFlightAssignmentPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
