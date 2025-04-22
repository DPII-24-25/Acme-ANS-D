/*
 * TechnicianTaskDeleteService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.entities.tasks.TaskType;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskDeleteService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Task task1;
		Technician technician;

		masterId = super.getRequest().getData("id", int.class);
		task1 = this.repository.findTaskById(masterId);
		technician = task1 == null ? null : task1.getTechnician();
		status = task1 != null && task1.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task1;
		int id;

		id = super.getRequest().getData("id", int.class);
		task1 = this.repository.findTaskById(id);

		super.getBuffer().addData(task1);
	}

	@Override
	public void bind(final Task object) {

		super.bindObject(object, "type", "description", "deadline", "priority", "estimatedDuration");

	}

	@Override
	public void validate(final Task object) {
		;
	}

	@Override
	public void perform(final Task object) {
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Task object) {
		SelectChoices choicesTypes;
		Dataset dataset;

		choicesTypes = SelectChoices.from(TaskType.class, object.getType());

		dataset = super.unbindObject(object, "type", "description", "deadline", "priority", "estimatedDuration", "technician.licenseNum", "draftMode");

		dataset.put("types", choicesTypes);

		super.getResponse().addData(dataset);
	}

}
