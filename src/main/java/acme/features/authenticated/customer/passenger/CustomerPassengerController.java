
package acme.features.authenticated.customer.passenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiController
public class CustomerPassengerController extends AbstractGuiController<Customer, Passenger> {

	@Autowired
	private CustomerPassengerListService	listService;
	@Autowired
	private CustomerPassengerShowService	showService;
	@Autowired
	private CustomerPassengerCreateService	createService;
	@Autowired
	private CustomerPassengerUpdateService	updateService;
	@Autowired
	private CustomerPassengerDeleteService	deleteService;


	@PostConstruct
	protected void initalise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

	}

}
