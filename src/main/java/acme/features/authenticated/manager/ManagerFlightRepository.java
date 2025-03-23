
package acme.features.authenticated.manager;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.entities.flight.Flight;

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

}
