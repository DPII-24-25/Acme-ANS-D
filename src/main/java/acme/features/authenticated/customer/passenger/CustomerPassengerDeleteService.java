
package acme.features.authenticated.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerDeleteService extends AbstractGuiService<Customer, Passenger> {

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
		Passenger object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findPassengerById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateBirth", "specialNeeds", "draftMode");
	}
	@Override
	public void perform(final Passenger object) {

		this.repository.delete(object);
	}
	@Override
	public void validate(final Passenger passenger) {
		;

	}

	@Override
	public void unbind(final Passenger object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbindObject(object, "fullName", "email", "passportNumber", "dateBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);

	}

}
