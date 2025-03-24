
package acme.features.assistance_agent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimUpdateService extends AbstractGuiService<AssistanceAgent, Claim> {

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
		Claim claim;
		int masterId = this.getRequest().getData("id", int.class);

		claim = this.repository.findClaimById(masterId);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {

		super.bindObject(claim, "tag", "selfTransfer", "description", "cost");
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void validate(final Claim claim) {
		;

	}

	@Override
	public void unbind(final Claim claim) {
		String dataset = null;
		super.getResponse().addData(dataset);
	}
}
