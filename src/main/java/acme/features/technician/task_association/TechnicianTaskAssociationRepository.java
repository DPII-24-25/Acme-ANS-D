/*
 * TechnicianTaskAssociationRepository.java
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

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenance_records.MaintenanceRecord;
import acme.entities.task_associations.TaskAssociation;
import acme.entities.tasks.Task;

@Repository
public interface TechnicianTaskAssociationRepository extends AbstractRepository {

	// TaskAssociation
	@Query("select ta from TaskAssociation ta where ta.id =:id")
	TaskAssociation findTaskAssociationById(int id);

	@Query("select ta from TaskAssociation ta where ta.maintenanceRecord.id =:maintenanceRecordId")
	Collection<TaskAssociation> findTaskAssociationsByMaintenanceRecordId(int maintenanceRecordId);

	// MaintenanceRecord
	@Query("select mr from MaintenanceRecord mr where mr.id =:recordId")
	MaintenanceRecord findMaintenanceRecordById(int recordId);

	// Task
	@Query("select t from Task t where t.id =:taskId")
	Task findTaskByTaskId(int taskId);

	@Query("select t from Task t where t.draftMode=false")
	Collection<Task> findAllAvailableTasks();

}
