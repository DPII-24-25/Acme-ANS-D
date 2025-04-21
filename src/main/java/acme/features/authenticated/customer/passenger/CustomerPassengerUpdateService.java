
package acme.features.authenticated.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerUpdateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository repository;


	@Override
	public void authorise() {
		int masterId;
		boolean status;
		boolean isOwner;
		boolean editable;
		Passenger passenger;

		masterId = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(masterId);
		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		isOwner = super.getRequest().getPrincipal().getActiveRealm().getId() == passenger.getBooking().getCustomer().getId();
		editable = passenger.isDraftMode();

		super.getResponse().setAuthorised(status && isOwner && editable);
	}

	@Override
	public void load() {
		Passenger passenger;
		int masterId = this.getRequest().getData("id", int.class);

		passenger = this.repository.findPassengerById(masterId);

		super.getBuffer().addData(passenger);
	}
	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateBirth", "specialNeeds", "draftMode");
	}
	@Override
	public void perform(final Passenger passenger) {
		this.repository.save(passenger);
	}
	@Override
	public void validate(final Passenger passenger) {
		;
	}
	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);
	}

}
