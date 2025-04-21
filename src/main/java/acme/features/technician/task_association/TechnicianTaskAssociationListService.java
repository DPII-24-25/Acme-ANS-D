/*
 * TechnicianTaskAssociationListService.java
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
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance_records.MaintenanceRecord;
import acme.entities.task_associations.TaskAssociation;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskAssociationListService extends AbstractGuiService<Technician, TaskAssociation> {

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

		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);

		record1 = this.repository.findMaintenanceRecordById(masterId);
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

		int masterId;
		Collection<TaskAssociation> associations;

		masterId = super.getRequest().getData("masterId", int.class);

		associations = this.repository.findTaskAssociationsByMaintenanceRecordId(masterId);

		super.getBuffer().addData(associations);
	}

	@Override
	public void unbind(final Collection<TaskAssociation> associations) {
		int masterId;
		MaintenanceRecord record1;

		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);

		record1 = this.repository.findMaintenanceRecordById(masterId);
		super.getResponse().addGlobal("draftMode", record1.isDraftMode());
	}

	@Override
	public void unbind(final TaskAssociation object) {
		Dataset dataset;

		dataset = super.unbindObject(object);
		dataset.put("task", object.getTask().getDescription());

		super.getResponse().addData(dataset);
	}

}
