
package acme.features.authenticated.manager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flight.Leg;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id =:id")
	Collection<Leg> findAllLegsByFlightId(int id);

	@Query("select al from ActivityLog al where al.leg.id =:id")
	Collection<ActivityLog> findAllActivityLogsByLegId(int id);

	@Query("select fa from FlightAssignment fa where fa.flightLeg.id =:id")
	Collection<FlightAssignment> findAllFlightAssignmentByLegId(int id);

}
