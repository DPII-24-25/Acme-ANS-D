/*
 * AdministratorAirportController.java
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

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.airline.Airline;

@GuiController
public class AdministratorAirlineController extends AbstractGuiController<Administrator, Airline> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineListService		listService;

	@Autowired
	private AdministratorAirlineShowService		showService;

	@Autowired
	private AdministratorAirlineCreateService	createService;

	@Autowired
	private AdministratorAirlineUpdateService	updateService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}

}
