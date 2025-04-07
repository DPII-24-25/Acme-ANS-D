/*
 * TechnicianTaskAssociationController.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.technician.task_association;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.task_associations.TaskAssociation;
import acme.realms.Technician;

@GuiController
public class TechnicianTaskAssociationController extends AbstractGuiController<Technician, TaskAssociation> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskAssociationListService	listService;

	@Autowired
	private TechnicianTaskAssociationShowService	showService;

	@Autowired
	private TechnicianTaskAssociationCreateService	createService;

	@Autowired
	private TechnicianTaskAssociationDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}

}
