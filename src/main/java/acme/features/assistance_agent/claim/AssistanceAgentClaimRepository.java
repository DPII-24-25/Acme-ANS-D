
package acme.features.assistance_agent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id =:id")
	Claim findClaimById(int id);

	@Query("select c from Claim c where c.assistanceAgent.id =:id")
	Collection<Claim> findClaimsByAssistanceAgentId(int id);

}
