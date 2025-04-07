/*
 * TechnicianMaintenanceRecordShowService.java
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
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance_records.MaintenanceRecord;
import acme.entities.maintenance_records.MaintenanceStatus;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordShowService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MaintenanceRecord record1;
		Technician technician;
		//Date currentMoment;
		int technicianId;
		//		boolean isMine;

		masterId = super.getRequest().getData("id", int.class);
		record1 = this.repository.findMaintenanceRecordById(masterId);
		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean owner = technicianId == record1.getTechnician().getId();
		technician = record1 == null ? null : record1.getTechnician();
		//currentMoment = MomentHelper.getCurrentMoment();
		if (record1.isDraftMode()) {
			status = super.getRequest().getPrincipal().hasRealm(technician) || record1 != null;
			status = owner;
		} else
			status = true;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord record1;
		int id;

		id = super.getRequest().getData("id", int.class);
		record1 = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(record1);
	}

	@Override
	public void unbind(final MaintenanceRecord record1) {
		//int technicianId;
		Collection<Aircraft> aircrafts;
		SelectChoices choices;
		SelectChoices choicesStatus;
		Dataset dataset;

		aircrafts = this.repository.findAllAircrafts();

		choices = SelectChoices.from(aircrafts, "registrationNumber", record1.getAircraft());
		choicesStatus = SelectChoices.from(MaintenanceStatus.class, record1.getStatus());

		dataset = super.unbindObject(record1, "moment", "status", "inspectDueDate", "estCost", "moreInfo", "draftMode");
		dataset.put("aircraft", choices.getSelected().getKey());
		dataset.put("aircrafts", choices);
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}

}
