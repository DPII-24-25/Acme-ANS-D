
package acme.features.authenticated.manager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.aircraft.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flight.Flight;
import acme.entities.flight.Leg;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id =:id")
	Collection<Leg> findAllLegsByFlightId(int id);

	@Query("select al from ActivityLog al where al.flightAssignment.leg.id =:id")
	Collection<ActivityLog> findAllActivityLogsByLegId(int id);

	@Query("select fa from FlightAssignment fa where fa.leg.id =:id")
	Collection<FlightAssignment> findAllFlightAssignmentByLegId(int id);

	@Query("select l from Leg l where l.id=:id")
	Leg findLegId(int id);

	@Query("select l from Leg l where l.flight.airline.manager.id =:id")
	Collection<Leg> findLegsByManagerId(int id);

	@Query("select f from Flight f where f.airline.id =:id")
	Collection<Flight> findAllFlightsByAirlineId(int id);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("select a from Aircraft a where a.id=:id")
	Aircraft findAircraftById(Integer id);

	@Query("select a from Airport a where a.id=:id")
	Airport findAirportById(Integer id);

	@Query("select f from Flight f where f.id =:id")
	Flight findFlightById(int id);

}
