/*
 * AdministratorAircraftCreateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.aircraft;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;

@GuiService
public class AdministratorAircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Aircraft Aircraft;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		Aircraft = new Aircraft();

		super.getBuffer().addData(Aircraft);
	}

	@Override
	public void bind(final Aircraft Aircraft) {
		super.bindObject(Aircraft, "name", "iataCode", "website", "type", "foundationMoment", "emailAdress", "phoneNumber");
	}

	@Override
	public void validate(final Aircraft Aircraft) {

	}

	@Override
	public void perform(final Aircraft Aircraft) {

		this.repository.save(Aircraft);
	}

	@Override
	public void unbind(final Aircraft Aircraft) {
		SelectChoices choices;
		Dataset dataset;

		dataset = super.unbindObject(Aircraft, "name", "iataCode", "website", "type", "foundationMoment", "emailAdress", "phoneNumber");

		super.getResponse().addData(dataset);
	}

}
