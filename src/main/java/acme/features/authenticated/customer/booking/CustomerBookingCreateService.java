
package acme.features.authenticated.customer.booking;

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
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository repository;


	@Override
	public void authorise() {

		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Booking obj;
		Customer customer;
		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		obj = new Booking();
		obj.setDraftMode(true);
		obj.setCustomer(customer);
		super.getBuffer().addData(obj);

	}

	@Override
	public void bind(final Booking booking) {

		super.bindObject(booking, "locatorCode", "purchaseMoment", "price", "creditCard", "draftMode");
		if (booking.getPurchaseMoment() == null)
			booking.setPurchaseMoment(MomentHelper.getCurrentMoment());

		// Obtener el valor del formulario
		String travelClassString = super.getRequest().getData("travelClass", String.class);
		if (travelClassString != null)
			try {
				// Convertir el String a Enum
				booking.setTravelClass(TravelClass.valueOf(travelClassString));
			} catch (IllegalArgumentException e) {
				super.state(false, "travelClass", "customer.booking.form.error.invalid-travel-class");
			}

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
			System.out.println(identifier);
			System.out.println(initials);
			boolean startsWithInitials = identifier.startsWith(initials);
			super.state(startsWithInitials, "identifier", "customer.booking.form.error.mismatched-initials");
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
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCard", "draftMode");
		dataset.put("travelClass", choices);
		super.getResponse().addData(dataset);
	}

}
