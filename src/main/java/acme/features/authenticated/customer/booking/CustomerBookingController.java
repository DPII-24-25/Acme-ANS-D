
package acme.features.authenticated.customer.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.customers.Booking;
import acme.realms.Customer;

@GuiController
public class CustomerBookingController extends AbstractGuiController<Customer, Booking> {

	@Autowired
	private CustomerBookingListService		listService;
	@Autowired
	private CustomerBookingShowService		showService;
	@Autowired
	private CustomerBookingCreateService	createService;


	@PostConstruct
	protected void initalise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);

	}

}
