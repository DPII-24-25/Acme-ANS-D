
package acme.features.authenticated.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Booking;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository repository;


	@Override
	public void authorise() {
		int bookingId = this.getRequest().getData("masterId", int.class);
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();

		boolean isOwner = this.repository.findCustomerIdByBookingId(bookingId) == customerId;

		super.getResponse().setAuthorised(isOwner);
	}

	@Override
	public void load() {
		final Integer masterId = super.getRequest().getData("masterId", Integer.class);

		if (masterId == null) {
			super.getResponse().setAuthorised(false);
			return;
		}

		Passenger obj = new Passenger();
		Booking booking = this.repository.findBookingById(masterId.intValue());

		obj.setDraftMode(true);
		obj.setBooking(booking);

		super.getBuffer().addData(obj);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateBirth", "specialNeeds");
		Integer masterId = super.getRequest().getData("masterId", Integer.class);
		if (masterId != null) {
			Booking booking = this.repository.findBookingById(masterId);
			passenger.setBooking(booking);
		}

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
		dataset = super.unbindObject(passenger, "booking", "fullName", "email", "passportNumber", "dateBirth", "specialNeeds", "draftMode");
		Integer masterId = super.getRequest().getData("masterId", Integer.class);
		dataset.put("masterId", masterId);
		super.getResponse().addData(dataset);
	}

}
