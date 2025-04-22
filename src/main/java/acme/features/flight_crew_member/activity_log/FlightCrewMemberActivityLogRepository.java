
package acme.features.flight_crew_member.activity_log;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.id =:id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select a from ActivityLog a where a.flightAssignment.id =:id")
	Collection<ActivityLog> findActivityLogsByFlightAssignmentId(int id);

	@Query("select a from ActivityLog a where a.id =:id")
	ActivityLog findActivityLogById(int id);

}
