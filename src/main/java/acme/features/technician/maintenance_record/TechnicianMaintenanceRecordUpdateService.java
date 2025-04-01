/*
 * TechnicianMaintenanceRecordUpdateService.java
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

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance_records.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordUpdateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	//@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractService<Technician, MaintenanceRecord> -------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MaintenanceRecord record1;
		Technician technician;

		masterId = super.getRequest().getData("id", int.class);
		record1 = this.repository.findMaintenanceRecordById(masterId);
		technician = record1 == null ? null : record1.getTechnician();
		status = record1 != null && record1.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

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

		//technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		aircrafts = this.repository.findAllAircrafts();
		choices = SelectChoices.from(aircrafts, "aircraft", record1.getAircraft());

		dataset = super.unbindObject(record1, "moment", "status", "inspectDueDate", "estCost", "moreInfo", "draftMode");
		dataset.put("aircraft", choices.getSelected().getKey());
		dataset.put("aircrafts", choices);

		super.getResponse().addData(dataset);
	}

}
