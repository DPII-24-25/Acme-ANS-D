
package acme.features.assistance_agent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.realms.AssistanceAgent;
import acme.realms.Manager;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim = new Claim();

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim object) {

	}

	@Override
	public void perform(final Claim object) {
		this.repository.save(object);
	}

	@Override
	public void validate(final Claim object) {

	}

	@Override
	public void unbind(final Claim object) {
		String dataset = null;
		super.getResponse().addData(dataset);
	}
}
