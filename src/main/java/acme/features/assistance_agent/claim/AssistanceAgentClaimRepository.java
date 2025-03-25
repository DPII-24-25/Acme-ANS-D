
package acme.features.assistance_agent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.flight.Flight;
import acme.entities.flight.Leg;
import acme.entities.trackinglog.TrackingLog;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id =:id")
	Claim findClaimById(int id);

	@Query("select c from Claim c where c.assistanceAgent.id =:id")
	Collection<Claim> findClaimsByAssistanceAgentId(int id);

	@Query("select f from Flight f")
	Collection<Flight> findFlights();

	@Query("select l from Leg l where l.id =:id")
	Leg findLegById(int id);

	@Query("select l from Leg l where l.flight.airline.id =:id")
	Collection<Leg> findLegsByAirlineId(int id);

	@Query("select a from AssistanceAgent a where a.id =:id")
	AssistanceAgent findAssistanceAgentById(int id);

	@Query("select t from TrackingLog t where t.claim.id =:id")
	Collection<TrackingLog> findTrackingLogsByClaimId(int id);

}
