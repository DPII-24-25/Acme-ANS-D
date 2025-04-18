
package acme.entities.maintenance_records;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface MaintenanceRecordRepository extends AbstractRepository {

	@Query("select mr.moment from MaintenanceRecord mr where mr = :record")
	Date findMomentByMR(MaintenanceRecord record);

	@Query("select mr.inspectDueDate from MaintenanceRecord mr where mr = :record")
	Date findDueDateByMR(MaintenanceRecord record);

}
