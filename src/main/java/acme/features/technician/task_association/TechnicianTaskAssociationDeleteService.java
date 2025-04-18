/*
 * TechnicianTaskAssociationDeleteService.java
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
import acme.entities.task_associations.TaskAssociation;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskAssociationDeleteService extends AbstractGuiService<Technician, TaskAssociation> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskAssociationRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Technician technician;
		TaskAssociation association;

		id = super.getRequest().getData("id", int.class);
		association = this.repository.findTaskAssociationById(id);

		technician = association == null ? null : association.getRecord().getTechnician();
		status = association != null && association.getRecord().isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

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
	public void bind(final TaskAssociation object) {
		Task task;
		int taskId;

		taskId = super.getRequest().getData("task", int.class);

		super.bindObject(object);

		task = this.repository.findTaskByTaskId(taskId);
		object.setTask(task);
	}

	@Override
	public void validate(final TaskAssociation object) {
		;
	}

	@Override
	public void perform(final TaskAssociation object) {
		this.repository.delete(object);
	}

	@Override
	public void unbind(final TaskAssociation object) {
		Collection<Task> tasks;
		SelectChoices choices;
		Dataset dataset;

		tasks = this.repository.findAllAvailableTasks();
		choices = SelectChoices.from(tasks, "description", object.getTask());

		dataset = super.unbindObject(object);
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);

		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("draftMode", object.getRecord().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
