
package acme.entities.Flight;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select min(l.scheduleDeparture) from Leg l where l.flight.id =:id")
	Date getScheduleDeparture(int id);

	@Query("select max(l.scheduleArrival) from Leg l where l.flight.id =:id")
	Date getScheduleArrivals(int id);

	@Query("SELECT l.airportDeparture FROM Leg l " + "WHERE l.flight.id = :id AND l.scheduleDeparture = " + "(SELECT MIN(l2.scheduleDeparture) FROM Leg l2 WHERE l2.flight.id = :id)")
	String getDepartureCity(int id);

	@Query("SELECT l.airportArrival FROM Leg l " + "WHERE l.flight.id = :id AND l.scheduleArrival = " + "(SELECT MAX(l2.scheduleArrival) FROM Leg l2 WHERE l2.flight.id = :id)")
	String getArrivalCity(int id);

	@Query("SELECT COUNT(l) - 1 FROM Leg l WHERE l.flight.id = :id")
	Integer getLayovers(int id);

}
