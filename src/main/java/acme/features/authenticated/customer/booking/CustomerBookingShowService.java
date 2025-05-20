
package acme.features.authenticated.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Booking;
import acme.entities.customers.BookingRepository;
import acme.entities.customers.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int customerId;
		Booking booking;

		customerId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(customerId);

		Customer current = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		status = booking != null && booking.getCustomer().equals(current);

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		Booking booking;
		int id;
		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);
		super.getBuffer().addData(booking);
	}
	@Override
	public void unbind(final Booking booking) {
		SelectChoices choices;
		SelectChoices choices2;
		Dataset dataset;
		Collection<Flight> publishedFlights = this.repository.findAllPublishedFlights();
		choices2 = SelectChoices.from(publishedFlights, "label", booking.getFlight());
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "price", "creditCard", "draftMode", "flight");
		dataset.put("flight", choices2.getSelected().getKey());
		dataset.put("publishedFlights", choices2);
		dataset.put("travelClass", choices.getSelected().getKey());
		dataset.put("travelClasss", choices);
		super.getResponse().addData(dataset);
	}

}
