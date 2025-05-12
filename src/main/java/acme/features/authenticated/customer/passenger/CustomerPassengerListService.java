
package acme.features.authenticated.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		super.getResponse().setAuthorised(true);
	}


	private int masterId;


	@Override
	public void load() {
		Collection<Passenger> passengers;
		this.masterId = super.getRequest().getData("masterId", int.class);
		passengers = this.repository.findPassengersByBookingId(this.masterId);

		// Añadir el masterId como variable global para la vista JSP
		super.getResponse().addGlobal("masterId", this.masterId);

		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "dateBirth", "specialNeeds", "passportNumber", "email");

		super.getResponse().addData(dataset);
	}

}
