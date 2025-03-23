
package acme.entities.flight;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;

import acme.client.repositories.AbstractRepository;
import acme.entities.airports.Airport;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select min(l.scheduleDeparture) from Leg l where l.flight.id =:id")
	Optional<Date> getScheduleDeparture(int id);

	@Query("select max(l.scheduleArrival) from Leg l where l.flight.id =:id")
	Optional<Date> getScheduleArrivals(int id);

	@Query("SELECT l.departureAirport FROM Leg l WHERE l.flight.id = :id AND l.scheduleDeparture = (SELECT MIN(l2.scheduleDeparture) FROM Leg l2 WHERE l2.flight.id = :id)")
	Optional<Airport> getDepartureCity(int id);

	@Query("SELECT l.arrivalAirport FROM Leg l WHERE l.flight.id = :id AND l.scheduleArrival = (SELECT MAX(l2.scheduleArrival) FROM Leg l2 WHERE l2.flight.id = :id)")
	Optional<Airport> getArrivalCity(int id);

	@Query("SELECT COUNT(l) - 1 FROM Leg l WHERE l.flight.id = :id")
	Optional<Integer> getLayovers(int id);

	@Query("SELECT a.iataCode FROM Airline a WHERE a.id = (SELECT l.flight.airline.id FROM Leg l WHERE l.id = :id)")
	String getIataCodeFromLegId(int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :id ORDER BY l.scheduleDeparture ASC")
	Collection<Leg> getLegsOrderedByDeparture(int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :id ORDER BY l.scheduleDeparture ASC")
	Collection<Leg> getLegsOrderedByArrival(int id);

}
