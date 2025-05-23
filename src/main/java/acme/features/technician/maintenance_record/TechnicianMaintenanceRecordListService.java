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

package acme.features.technician.maintenance_record;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance_records.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordListService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> records;
		int technicianId;
		boolean isMine;

		isMine = super.getRequest().getData("mine", boolean.class);
		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (isMine)
			records = this.repository.findMaintenanceRecordByTechnicianId(technicianId);
		else
			records = this.repository.findAllPublishedMaintenanceRecordById();

		super.getBuffer().addData(records);
	}

	@Override
	public void unbind(final MaintenanceRecord record1) {
		Dataset dataset;

		dataset = super.unbindObject(record1, "status", "aircraft.registrationNumber", "inspectDueDate", "draftMode");

		super.getResponse().addData(dataset);
	}

}
