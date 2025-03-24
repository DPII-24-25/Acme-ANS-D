
package acme.features.assistance_agent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.flight.Flight;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id =:id")
	Claim findClaimById(int id);

	@Query("select c from Claim c where c.assistanceAgent.id =:id")
	Collection<Claim> findClaimsByAssistanceAgentId(int id);

	@Query("select f from Flight f")
	Collection<Flight> findFlights();

	@Query("select f from Flight f where f.airline.id =:id")
	Collection<Flight> findFlightsByAirline(int id);

	@Query("select a from AssistanceAgent a where a.id =:id")
	AssistanceAgent findAssistanceAgentById(int id);

}
