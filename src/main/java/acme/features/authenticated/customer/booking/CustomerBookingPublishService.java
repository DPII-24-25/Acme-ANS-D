
package acme.features.authenticated.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Booking;
import acme.entities.customers.BookingRepository;
import acme.entities.customers.Passenger;
import acme.entities.customers.TravelClass;
import acme.entities.flight.Flight;
import acme.features.authenticated.customer.passenger.PassengerRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository	repository;
	@Autowired
	private PassengerRepository	passengerRepo;


	@Override
	public void authorise() {
		boolean status;
		int customerId;
		Booking booking;

		customerId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(customerId);

		Customer current = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		status = booking != null && booking.getCustomer().equals(current) && booking.isDraftMode();

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		Booking booking;
		int masterId = this.getRequest().getData("id", int.class);

		booking = this.repository.findBookingById(masterId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		super.bindObject(booking, "locatorCode", "travelClass", "creditCard");

		booking.setFlight(flight);
	}
	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		this.repository.findAllPassengerByBookingId(booking.getId()).stream().forEach(x -> {
			x.setDraftMode(false);
			this.passengerRepo.save(x);
		});
		this.repository.save(booking);
	}

	@Override
	public void validate(final Booking booking) {
		if (!super.getBuffer().getErrors().hasErrors("creditCard")) {
			String card = booking.getCreditCard();
			super.state(!card.isBlank(), "creditCard", "javax.validation.constraints.NotNull.message");
		}
		if (!super.getBuffer().getErrors().hasErrors("locatorCode")) {
			String locatorCode = booking.getLocatorCode();
			Booking existingBooking = this.repository.findByLocatorCode(locatorCode);

			boolean duplicate = existingBooking != null && existingBooking.getId() != booking.getId();

			super.state(!duplicate, "locatorCode", "customer.booking.form.error.duplicateLocator");
		}
		Collection<Passenger> allPassenger = this.passengerRepo.findPassengersByBookingId(booking.getId());
		System.out.println(allPassenger);
		if (allPassenger.isEmpty())
			super.state(false, "*", "customer.booking.form.error.noPassengers");
		if (!super.getBuffer().getErrors().hasErrors("flight"))
			super.state(booking.getFlight() != null, "flight", "javax.validation.constraints.NotNull.message");

	}
	@Override
	public void unbind(final Booking booking) {
		SelectChoices choices, choices2;
		Dataset dataset;
		Collection<Flight> publishedFlights = this.repository.findAllPublishedFlights();

		choices2 = SelectChoices.from(publishedFlights, "label", booking.getFlight());

		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "purchaseMoment", "price", "creditCard", "flight", "draftMode");
		dataset.put("flight", choices2.getSelected().getKey());
		dataset.put("publishedFlights", choices2);
		dataset.put("travelClass", choices.getSelected().getKey());
		dataset.put("travelClasss", choices);
		super.getResponse().addData(dataset);
	}

}
