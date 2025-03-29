/*
 * WorkerJobShowService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.manager;

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
public class ManagerFlightShowService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		int masterId = this.getRequest().getData("id", int.class);
		int managerId = this.getRequest().getPrincipal().getActiveRealm().getId();

		boolean isOwner = this.repository.findFlightId(masterId).getAirline().getManager().getId() == managerId;
		super.getResponse().setAuthorised(isOwner);
	}

	@Override
	public void load() {
		Flight flight;
		int masterId = this.getRequest().getData("id", int.class);

		flight = this.repository.findFlightId(masterId);

		super.getBuffer().addData(flight);
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
