
package acme.features.assistance_agent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

	}

	@Override
	public void bind(final Claim claim) {

	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void validate(final Claim claim) {

	}

	@Override
	public void unbind(final Claim claim) {
		String dataset = null;

		super.getResponse().addData(dataset);
	}
}
