/*
 * TechnicianTaskListService.java
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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListService extends AbstractGuiService<Technician, Task> {

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
		Collection<Task> tasks;
		int technicianId;
		boolean isMine;

		isMine = super.getRequest().getData("mine", boolean.class);
		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (isMine)
			tasks = this.repository.findTaskByTechnicianId(technicianId);
		else
			tasks = this.repository.findAllPublishedTasks();

		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Task task1) {
		Dataset dataset;

		dataset = super.unbindObject(task1, "type", "deadline", "priority", "draftMode");

		super.getResponse().addData(dataset);
	}

}
