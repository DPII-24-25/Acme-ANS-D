
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerFlightCreateService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		final String method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = true;
		else if (method.equals("POST")) {
			status = super.getRequest().getData("id", int.class) == 0;
			if (super.getRequest().getData().containsKey("airline")) {
				final int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
				final int airlineId = super.getRequest().getData("airline", int.class) == 0 ? 0 : super.getRequest().getData("airline", int.class);
				final Airline airline = this.repository.findAirlineById(airlineId);
				if (airline == null)
					status = false;
				else
					status = status && airline.getManager().getId() == managerId;

			}
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Flight flight = new Flight();

		flight.setDraft(true);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight object) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(object, "tag", "selfTransfer", "description", "cost");
		object.setAirline(airline);

	}

	@Override
	public void perform(final Flight object) {
		this.repository.save(object);
	}

	@Override
	public void validate(final Flight object) {
		;
	}

	@Override
	public void unbind(final Flight object) {
		Manager manager;
		SelectChoices choices;
		Collection<Airline> airlines = null;
		Dataset dataset;
		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		airlines = this.repository.findAirlinesByManager(manager.getId());

		dataset = super.unbindObject(object, "tag", "selfTransfer", "description", "cost", "draft");

		choices = SelectChoices.from(airlines, "iataCode", object.getAirline());

		dataset.put("airline", choices.getSelected().getKey());
		dataset.put("airlines", choices);

		super.getResponse().addData(dataset);
	}
}
