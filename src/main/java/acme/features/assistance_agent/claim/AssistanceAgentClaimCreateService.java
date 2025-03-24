
package acme.features.assistance_agent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

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
		Claim claim = new Claim();

		AssistanceAgent assistanceAgent;

		assistanceAgent = this.repository.findAssistanceAgentById(super.getRequest().getPrincipal().getActiveRealm().getId());

		claim.setAssistanceAgent(assistanceAgent);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim object) {
		/*
		 * assert object != null;
		 * 
		 * int claimId;
		 * Flight flight;
		 * 
		 * claimId = super.getRequest().getData("claim", int.class);
		 * claim = this.repository.findOneProjectById(projectId);
		 * super.bind(object, "code", "providerName", "customerName", "goals", "budget");
		 * final Date cMoment = MomentHelper.getCurrentMoment();
		 * object.setInstantiationMoment(cMoment);
		 * object.setProject(project);
		 */
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
