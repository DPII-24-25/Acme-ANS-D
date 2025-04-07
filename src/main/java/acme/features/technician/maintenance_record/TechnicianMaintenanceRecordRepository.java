/*
 * TechnicianMaintenanceRecordRepository.java
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

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance_records.MaintenanceRecord;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.id =:id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select t from Technician t where t.id =:id")
	Technician findTechnicianById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.technician.id =:id")
	Collection<MaintenanceRecord> findMaintenanceRecordByTechnicianId(int id);

	@Query("select mr from MaintenanceRecord mr where mr.draftMode=false")
	Collection<MaintenanceRecord> findAllPublishedMaintenanceRecordById();

	@Query("select ta.task from TaskAssociation ta where ta.record.id =:maintenanceRecordId")
	Collection<Task> findTasksByMaintenanceRecordId(int maintenanceRecordId);

	@Query("select count(ta.task) from TaskAssociation ta where ta.record.id =:maintenanceRecordId and ta.task.draftMode = true")
	Integer findTotalNotPublishedTasksByMaintenanceRecordId(int maintenanceRecordId);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select a from Aircraft a where a.id = :aircraftId")
	Aircraft findAircraftById(int aircraftId);

}
