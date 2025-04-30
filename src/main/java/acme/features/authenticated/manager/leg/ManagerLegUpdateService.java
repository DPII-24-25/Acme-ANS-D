
package acme.features.authenticated.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flight.Flight;
import acme.entities.flight.Leg;
import acme.entities.flight.Leg.Status;
import acme.features.authenticated.manager.flight.ManagerFlightRepository;
import acme.realms.Manager;

@GuiService
public class ManagerLegUpdateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerFlightRepository	flightRepository;
	@Autowired
	private ManagerLegRepository	repository;


	@Override
	public void authorise() {
		int masterId;
		boolean status;
		boolean isOwner;
		boolean editable;
		Leg leg;

		masterId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegId(masterId);
		isOwner = super.getRequest().getPrincipal().getActiveRealm().getId() == leg.getFlight().getAirline().getManager().getId();
		editable = leg.isDraftMode();

		super.getResponse().setAuthorised(isOwner && editable);
	}

	@Override
	public void load() {
		Leg leg;
		int masterId = this.getRequest().getData("id", int.class);

		leg = this.repository.findLegId(masterId);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {

		super.bindObject(leg, "status", "flightNumber", "scheduleDeparture", "scheduleArrival");
		leg.setAircraft(this.repository.findAircraftById(super.getRequest().getData("aircraft", int.class)));
		leg.setArrivalAirport(this.repository.findAirportById(super.getRequest().getData("arrivalAirport", int.class)));
		leg.setDepartureAirport(this.repository.findAirportById(super.getRequest().getData("departureAirport", int.class)));

		leg.setFlight(this.flightRepository.findFlightId(super.getRequest().getData("flight", int.class)));
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void validate(final Leg leg) {

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		SelectChoices statusChoices;
		SelectChoices flightsChoices;
		SelectChoices aircraftChoices;
		SelectChoices airportArrivalsChoices;
		SelectChoices airportDepartureChoices;
		Collection<Airport> airports;
		Collection<Flight> flights;
		Collection<Aircraft> aircrafts;
		int airlineId;

		airlineId = leg.getFlight().getAirline().getId();

		flights = this.repository.findAllFlightsByAirlineId(airlineId);
		aircrafts = this.repository.findAllAircrafts();
		airports = this.repository.findAllAirports();

		statusChoices = SelectChoices.from(Status.class, leg.getStatus());
		flightsChoices = SelectChoices.from(flights, "tag", leg.getFlight());
		airportArrivalsChoices = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		airportDepartureChoices = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());

		dataset = super.unbindObject(leg, "flightNumber", "scheduleDeparture", "scheduleArrival", "status", "draftMode");

		dataset.put("statusOptions", statusChoices);
		dataset.put("flightOptions", flightsChoices);
		dataset.put("flight", flightsChoices.getSelected().getKey());
		dataset.put("aircraftOptions", aircraftChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("departureAirports", airportDepartureChoices);
		dataset.put("departureAirport", airportDepartureChoices.getSelected().getKey());
		dataset.put("arrivalAirports", airportArrivalsChoices);
		dataset.put("arrivalAirport", airportArrivalsChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
