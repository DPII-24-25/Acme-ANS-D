
package acme.entities.customers;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.components.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;

public interface BookingRepository extends AbstractRepository {

	@Query("SELECT b.creditCard FROM Booking b WHERE b.id = :bookingId")
	String findCreditCardByBookingId(int bookingId);

	@Query("select b from Booking b where b.customer.id = :id")
	Collection<Booking> findBookingsById(int id);

	@Query("select b from Booking b where b.id =:id")
	Booking findBookingById(int id);

	@Query("select f from Flight f where f.isDraft = false")
	Collection<Flight> findAllPublishedFlights();

	@Query("SELECT f FROM Flight f WHERE f.id = :id")
	Flight findFlightById(int id);

	@Query("SELECT p FROM Passenger p WHERE p.booking.id = :id")
	Collection<Passenger> findAllPassengerByBookingId(int id);

	@Query("SELECT f.cost FROM Flight f WHERE f.id = :flightId")
	Money findCostByFlightBooking(int flightId);
	@Query("SELECT COUNT(f) > 0 FROM Flight f WHERE f.id = :flightId AND f.isDraft = false")
	boolean isFlightPublished(int flightId);

	@Query("Select COUNT(b) > 0 from Booking b WHERE b.locatorCode = :locatorCode")
	boolean existsByLocatorCode(String locatorCode);
	@Query("SELECT b FROM Booking b WHERE b.locatorCode = :locatorCode")
	Booking findByLocatorCode(String locatorCode);

}
