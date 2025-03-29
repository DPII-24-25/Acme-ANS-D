
package acme.features.authenticated.manager.flight;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerFlightPublishService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		int masterId;
		boolean status;
		boolean isOwner;
		boolean editable;
		Flight flight;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightId(masterId);
		status = super.getRequest().getPrincipal().hasRealmOfType(Manager.class);
		isOwner = super.getRequest().getPrincipal().getActiveRealm().getId() == flight.getAirline().getManager().getId();
		editable = flight.isDraft();

		super.getResponse().setAuthorised(status && isOwner && editable);
	}

	@Override
	public void load() {
		Flight flight;
		int masterId = this.getRequest().getData("id", int.class);

		flight = this.repository.findFlightId(masterId);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(flight, "tag", "selfTransfer", "description", "cost", "draft");
		flight.setAirline(airline);

	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraft(false);
		this.repository.save(flight);
	}

	@Override
	public void validate(final Flight flight) {
		boolean oneLeg;
		boolean allLegsPublished;

		oneLeg = this.repository.findNumberLegsByFlightId(flight.getId()) >= 1 ? true : false;
		allLegsPublished = this.repository.findAllLegsByFLightId(flight.getId()).stream().allMatch(x -> x.isDraftMode() == false);

		super.state(!allLegsPublished, "*", "flight.form.validation.legs");
		super.state(!oneLeg, "*", "flight.form.validation.oneLeg");
	}

	@Override
	public void unbind(final Flight flight) {
		Manager manager;
		SelectChoices choices;
		Collection<Airline> airlines = null;
		Dataset dataset;
		Date scheduleArrival;
		Date scheduleDeparture;
		Integer layovers;
		String arrivalCity;
		String departureCity;

		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		airlines = this.repository.findAirlinesByManager(manager.getId());

		choices = SelectChoices.from(airlines, "iataCode", flight.getAirline());
		scheduleArrival = flight.getScheduleArrivals();
		scheduleDeparture = flight.getScheduleDeparture();
		layovers = flight.getLayovers();
		arrivalCity = flight.getArrivalCity();
		departureCity = flight.getDepartureCity();
		dataset = super.unbindObject(flight, "tag", "selfTransfer", "description", "cost", "draft");
		dataset.put("scheduleArrival", scheduleArrival);
		dataset.put("scheduleDeparture", scheduleDeparture);
		dataset.put("arrivalCity", arrivalCity);
		dataset.put("departureCity", departureCity);
		if (layovers >= 0)
			dataset.put("layovers", layovers);
		else
			dataset.put("layovers", 0);
		dataset.put("airline", choices.getSelected().getKey());
		dataset.put("airlines", choices);

		super.getResponse().addData(dataset);
	}
}
