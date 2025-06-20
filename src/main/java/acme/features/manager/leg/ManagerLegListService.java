/*
 * ManagerLegListService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerLegListService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int masterId = this.getRequest().getData("flightId", int.class);
		int managerId = this.getRequest().getPrincipal().getActiveRealm().getId();

		boolean isOwner = this.repository.findFlightById(masterId).getAirline().getManager().getId() == managerId;

		super.getResponse().setAuthorised(isOwner);
	}

	@Override
	public void load() {
		Collection<Leg> legs;
		int flightId;

		flightId = super.getRequest().getData("flightId", int.class);

		legs = this.repository.findAllLegsByFlightId(flightId);

		super.getBuffer().addData(legs);

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		dataset = super.unbindObject(leg, "flightNumber", "scheduleDeparture", "scheduleArrival", "status", "departureAirport", "arrivalAirport", "aircraft", "flight", "draftMode");

		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<Leg> legs) {
		int flightId;
		flightId = super.getRequest().getData("flightId", int.class);

		boolean updateable = this.repository.findFlightById(flightId).isDraft();

		super.getResponse().addGlobal("updateable", updateable);
		super.getResponse().addGlobal("flightId", flightId);
	}

}
