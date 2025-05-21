
package acme.features.authenticated.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerShowService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository repository;


	@Override
	public void authorise() {
		int masterId = this.getRequest().getData("id", int.class);
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		boolean isOwner = this.repository.findPassengerById(masterId).getBooking().getCustomer().getId() == customerId;
		super.getResponse().setAuthorised(isOwner);
	}
	@Override
	public void load() {
		Passenger passenger;
		int id;
		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);
		super.getBuffer().addData(passenger);
	}
	@Override
	public void unbind(final Passenger passenger) {

		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "dateBirth", "specialNeeds", "passportNumber", "email", "draftMode", "booking");
		dataset.put("booking", passenger.getBooking().getLocatorCode());
		super.getResponse().addData(dataset);
	}

}
