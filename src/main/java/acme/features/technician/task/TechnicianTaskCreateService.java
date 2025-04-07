/*
 * TechnicianTaskCreateService.java
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
public class TechnicianTaskCreateService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Task task1;
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		task1 = new Task();
		task1.setDraftMode(true);
		task1.setTechnician(technician);

		super.getBuffer().addData(task1);
	}

	@Override
	public void bind(final Task task1) {

		super.bindObject(task1, "type", "description", "deadline", "priority", "estimatedDuration");

	}

	@Override
	public void validate(final Task task1) {
		;
	}

	@Override
	public void perform(final Task task1) {
		this.repository.save(task1);
	}

	@Override
	public void unbind(final Task task1) {
		SelectChoices choicesTypes;
		Dataset dataset;

		choicesTypes = SelectChoices.from(TaskType.class, task1.getType());

		dataset = super.unbindObject(task1, "type", "description", "deadline", "priority", "estimatedDuration", "technician.licenseNum", "draftMode");

		dataset.put("types", choicesTypes);

		super.getResponse().addData(dataset);
	}

}
