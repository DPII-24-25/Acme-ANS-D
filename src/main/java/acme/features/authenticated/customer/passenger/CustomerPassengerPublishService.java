
package acme.features.authenticated.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerPublishService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository repo;


	@Override
	public void authorise() {
		int masterId;
		boolean status;
		boolean isOwner;
		boolean editable;
		Passenger passenger;

		masterId = super.getRequest().getData("id", int.class);
		passenger = this.repo.findPassengerById(masterId);
		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		isOwner = super.getRequest().getPrincipal().getActiveRealm().getId() == passenger.getBooking().getCustomer().getId();
		editable = passenger.isDraftMode();

		super.getResponse().setAuthorised(status && isOwner && editable);
	}
	@Override
	public void load() {
		Passenger passenger;
		int masterId = this.getRequest().getData("id", int.class);

		passenger = this.repo.findPassengerById(masterId);

		super.getBuffer().addData(passenger);
	}
	@Override
	public void bind(final Passenger passenger) {

		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateBirth", "specialNeeds");

	}
	@Override
	public void perform(final Passenger passenger) {

		passenger.setDraftMode(false);

		this.repo.save(passenger);
	}
	@Override
	public void validate(final Passenger passenger) {
		assert passenger != null;

		Passenger existing = this.repo.findPassengerByBookingIdAndPassportNumber(passenger.getBooking().getId(), passenger.getPassportNumber());

		if (existing != null)
			super.state(false, "passportNumber", "customer.passenger.form.error.existingPassenger");
	}
	@Override
	public void unbind(final Passenger passenger) {

		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "dateBirth", "specialNeeds", "passportNumber", "email", "draftMode", "booking");
		dataset.put("booking", passenger.getBooking().getLocatorCode());
		super.getResponse().addData(dataset);
	}

}
