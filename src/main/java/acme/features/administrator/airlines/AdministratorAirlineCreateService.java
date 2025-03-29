/*
 * AdministratorAirlineCreateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.airlines;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineType;

@GuiService
public class AdministratorAirlineCreateService extends AbstractGuiService<Administrator, Airline> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airline airline;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		airline = new Airline();
		airline.setName("");
		airline.setIataCode("");
		airline.setWebsite("");
		airline.setFoundationMoment(moment);
		airline.setEmailAdress("");
		airline.setPhoneNumber("");

		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "emailAdress", "phoneNumber");
	}

	@Override
	public void validate(final Airline airline) {
		boolean confirmation;
		//boolean properLength;
		//boolean isInPast;
		boolean uniqueIataCode;

		//String name = super.getRequest().getData("name", String.class);
		//Date moment = super.getRequest().getData("foundationMoment", Date.class);
		String iataCode = super.getRequest().getData("iataCode", String.class);

		uniqueIataCode = !this.repository.findAllAirlines().stream().map(Airline::getIataCode).anyMatch(i -> i.equals(iataCode));
		super.state(uniqueIataCode, "iataCode", "acme.validation.airline.uniqueIataCode.message");

		//properLength = name.length() <= 50;
		//super.state(properLength, "name", "acme.validation.airline.nameLength.message");

		//isInPast = moment != null && (moment.before(MomentHelper.getCurrentMoment()) || moment.equals(MomentHelper.getCurrentMoment()));
		//super.state(isInPast, "foundationMoment", "acme.validation.airline.momentIsInPast.message");

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airline airline) {

		this.repository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(AirlineType.class, airline.getType());

		dataset = super.unbindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "emailAdress", "phoneNumber");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

}
