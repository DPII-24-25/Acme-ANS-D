
package acme.features.authenticated.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.customers.Booking;
import acme.entities.customers.Passenger;

public interface PassengerRepository extends AbstractRepository {

	@Query("Select p from Passenger p where p.booking.id =:id")
	Collection<Passenger> findPassengersByBookingId(int id);

	@Query("select b from Booking b where b.id =:id")
	Booking findBookingById(int id);

	@Query("select p from Passenger p where p.id =:id")
	Passenger findPassengerById(int id);

}
