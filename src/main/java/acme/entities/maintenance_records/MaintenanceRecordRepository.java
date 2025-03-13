
package acme.entities.maintenance_records;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceRecordRepository {

	@Query("select mr.moment from MaintenanceRecord mr where mr = : record")
	Date findMomentByMR(MaintenanceRecord mRecord);

	@Query("select mr.inspectDueDate from MaintenanceRecord mr where mr = : record")
	Date findDueDateByMR(MaintenanceRecord mRecord);

}
