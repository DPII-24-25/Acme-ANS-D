
package acme.entities.trackinglog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.resolution=100")
	Collection<TrackingLog> findTrackingLogs();

}
