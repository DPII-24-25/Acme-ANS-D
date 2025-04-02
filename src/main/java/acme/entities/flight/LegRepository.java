
package acme.entities.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface LegRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :id ORDER BY l.scheduleDeparture ASC")
	Collection<Leg> getLegsOrderedByDeparture(int id);
}
