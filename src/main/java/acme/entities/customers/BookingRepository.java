
package acme.entities.customers;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface BookingRepository extends AbstractRepository {

	@Query("SELECT b.creditCard FROM Booking b WHERE b.id = :bookingId")
	String findCreditCardByBookingId(int bookingId);

	@Query("select b from Booking b where b.customer.id = :id")
	Collection<Booking> findBookingsById(int id);

	@Query("select b from Booking b where b.id =:id")
	Booking findBookingById(int id);

}
