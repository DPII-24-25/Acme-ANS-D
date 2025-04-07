/*
 * TechnicianTaskAssociationShowService.java
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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance_records.MaintenanceRecord;
import acme.entities.task_associations.TaskAssociation;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskAssociationShowService extends AbstractGuiService<Technician, TaskAssociation> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskAssociationRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int masterId;
		int technicianId;
		MaintenanceRecord record1;
		boolean status;
		Technician technician;

		masterId = super.getRequest().getData("id", int.class);
		super.getResponse().addGlobal("id", masterId);

		record1 = this.repository.findTaskAssociationById(masterId).getRecord();
		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean owner = technicianId == record1.getTechnician().getId();
		technician = record1 == null ? null : record1.getTechnician();

		if (record1.isDraftMode()) {
			status = super.getRequest().getPrincipal().hasRealm(technician) || record1 != null;
			status = owner;
		} else
			status = true;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TaskAssociation association;
		int id;

		id = super.getRequest().getData("id", int.class);
		association = this.repository.findTaskAssociationById(id);

		super.getBuffer().addData(association);
	}

	@Override
	public void unbind(final TaskAssociation object) {
		SelectChoices choices;
		Collection<Task> tasks;
		Dataset dataset;

		tasks = this.repository.findAllAvailableTasks();
		choices = SelectChoices.from(tasks, "description", object.getTask());

		dataset = super.unbindObject(object);
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);
		dataset.put("draftMode", object.getRecord().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
