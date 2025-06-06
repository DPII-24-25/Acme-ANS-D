
package acme.features.authenticated.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Booking;
import acme.entities.customers.BookingRepository;
import acme.entities.customers.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		Booking booking;

		int bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);

		boolean hasFlightId = super.getRequest().hasData("flight", int.class);
		boolean isFlightAccessible = false;

		if (hasFlightId) {
			int flightId = super.getRequest().getData("flight", int.class);

			if (flightId != 0)
				isFlightAccessible = this.repository.isFlightPublished(flightId);
			else

				isFlightAccessible = true;
		} else

			isFlightAccessible = true;

		Customer current = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		status = booking != null && booking.getCustomer().equals(current) && booking.isDraftMode() && isFlightAccessible;

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
	public void validate(final Booking booking) {
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		DefaultUserIdentity identity = customer.getUserAccount().getIdentity();
		String name = identity.getName();
		String surname = identity.getSurname();
		String identifier = customer.getIdentifier();
		if (!super.getBuffer().getErrors().hasErrors("identifier")) {

			String initials = name.substring(0, 1).toUpperCase() + surname.substring(0, 1).toUpperCase();
			boolean startsWithInitials = identifier.startsWith(initials);
			super.state(startsWithInitials, "identifier", "customer.booking.form.error.mismatched-initials");
		}
		if (!super.getBuffer().getErrors().hasErrors("locatorCode")) {
			String locatorCode = booking.getLocatorCode();
			Booking existingBooking = this.repository.findByLocatorCode(locatorCode);

			boolean duplicate = existingBooking != null && existingBooking.getId() != booking.getId();

			super.state(!duplicate, "locatorCode", "customer.booking.form.error.duplicateLocator");
		}

	}

	@Override
	public void perform(final Booking booking) {
		Date moment;
		moment = MomentHelper.getCurrentMoment();
		booking.setPurchaseMoment(moment);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices choices, choices2;
		Dataset dataset;
		Collection<Flight> publishedFlights = this.repository.findAllPublishedFlights();

		choices2 = SelectChoices.from(publishedFlights, "label", booking.getFlight());

		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "creditCard", "draftMode");
		dataset.put("flight", choices2.getSelected().getKey());
		dataset.put("publishedFlights", choices2);
		dataset.put("travelClass", choices.getSelected().getKey());
		dataset.put("travelClasss", choices);
		super.getResponse().addData(dataset);
	}

}
