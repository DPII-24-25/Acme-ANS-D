
package acme.entities.aircraft;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AircraftRepository extends AbstractRepository {

	@Query("select a from Aircraft a where a.status = 'ACTIVE'")
	List<Aircraft> findAllActiveAircraft();

}
