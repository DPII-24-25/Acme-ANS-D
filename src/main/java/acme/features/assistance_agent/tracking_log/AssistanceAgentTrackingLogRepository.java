
package acme.features.assistance_agent.tracking_log;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.trackinglog.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id =:id")
	Claim findClaimById(int id);

	@Query("select t from TrackingLog t where t.claim.id =:id")
	Collection<TrackingLog> findTrackingLogsByClaimId(int id);

	@Query("select t from TrackingLog t where t.id =:id")
	TrackingLog findTrackingLogById(int id);

}
