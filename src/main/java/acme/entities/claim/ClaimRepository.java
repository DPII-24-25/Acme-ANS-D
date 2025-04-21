
package acme.entities.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trackinglog.TrackingLog;
import acme.entities.trackinglog.TypeStatus;

@Repository
public interface ClaimRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.id =:id")
	Collection<TrackingLog> findTrackingLogsByClaimId(int id);

	@Query("select t.status from TrackingLog t where t.claim.id =:id")
	Collection<TypeStatus> findTrackingLogStatusByClaimId(int id);

}
