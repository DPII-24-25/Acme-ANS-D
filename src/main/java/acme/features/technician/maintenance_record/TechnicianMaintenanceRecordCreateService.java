/*
 * TechnicianMaintenanceRecordCreateService.java
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
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

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
		MaintenanceRecord record1;
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		record1 = new MaintenanceRecord();
		record1.setDraftMode(true);
		record1.setTechnician(technician);

		super.getBuffer().addData(record1);
	}

	@Override
	public void bind(final MaintenanceRecord record1) {
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(record1, "moment", "status", "inspectDueDate", "estCost", "moreInfo");
		record1.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord record1) {
		;
	}

	@Override
	public void perform(final MaintenanceRecord record1) {
		this.repository.save(record1);
	}

	@Override
	public void unbind(final MaintenanceRecord record1) {
		//int technicianId;
		Collection<Aircraft> aircrafts;
		SelectChoices choices;
		Dataset dataset;
		SelectChoices choicesStatus;

		choicesStatus = SelectChoices.from(MaintenanceStatus.class, record1.getStatus());
		//technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		aircrafts = this.repository.findAllAircrafts();
		choices = SelectChoices.from(aircrafts, "registrationNumber", record1.getAircraft());

		dataset = super.unbindObject(record1, "moment", "status", "inspectDueDate", "estCost", "moreInfo", "draftMode");
		dataset.put("aircraft", choices.getSelected().getKey());
		dataset.put("aircrafts", choices);
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}

}
