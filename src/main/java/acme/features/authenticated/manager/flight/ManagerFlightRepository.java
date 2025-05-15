
package acme.features.authenticated.manager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.entities.flight.Flight;
import acme.entities.flight.Leg;

@Repository
public interface ManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id =:id")
	Flight findFlightId(int id);

	@Query("select f from Flight f where f.airline.manager.id =:id")
	Collection<Flight> findFlightsByManagerId(int id);

	@Query("select a from Airline a where a.manager.id =:id")
	Collection<Airline> findAirlinesByManager(int id);

	@Query("select a from Airline a where a.id =:id")
	Airline findAirlineById(int id);

	@Query("select COUNT(l) from Leg l where l.flight.id=:id")
	int findNumberLegsByFlightId(int id);

	@Query("select l from Leg l where l.flight.id=:id")
	Collection<Leg> findAllLegsByFLightId(int id);

}
