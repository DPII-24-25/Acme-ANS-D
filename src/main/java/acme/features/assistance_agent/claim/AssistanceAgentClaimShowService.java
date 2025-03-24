
package acme.features.assistance_agent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean auth = this.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(auth);
	}

	@Override
	public void load() {
		Claim claim;
		int masterId = this.getRequest().getData("id", int.class);

		claim = this.repository.findClaimById(masterId);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		String dataset = null;

		super.getResponse().addData(dataset);
	}

}
