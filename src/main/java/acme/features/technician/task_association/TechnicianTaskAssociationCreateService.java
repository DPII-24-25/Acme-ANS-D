/*
 * TechnicianTaskAssociationCreateService.java
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
public class TechnicianTaskAssociationCreateService extends AbstractGuiService<Technician, TaskAssociation> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskAssociationRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		masterId = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
		technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnician();
		status = maintenanceRecord != null && maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int masterId;
		TaskAssociation association;
		MaintenanceRecord record1;

		association = new TaskAssociation();

		masterId = super.getRequest().getData("masterId", int.class);
		record1 = this.repository.findMaintenanceRecordById(masterId);

		association.setRecord(record1);

		super.getBuffer().addData(association);
	}

	@Override
	public void bind(final TaskAssociation association) {
		Task task;
		int taskId;

		taskId = super.getRequest().getData("task", int.class);

		super.bindObject(association);

		task = this.repository.findTaskByTaskId(taskId);
		association.setTask(task);
	}

	@Override
	public void validate(final TaskAssociation association) {
		;
	}

	@Override
	public void perform(final TaskAssociation association) {
		this.repository.save(association);
	}

	@Override
	public void unbind(final TaskAssociation association) {
		Collection<Task> tasks;
		SelectChoices choices;
		Dataset dataset;

		tasks = this.repository.findAllAvailableTasks();
		choices = SelectChoices.from(tasks, "description", association.getTask());

		dataset = super.unbindObject(association);
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}

}
