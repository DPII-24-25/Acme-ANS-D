
package acme.features.assistance_agent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.IssuesType;
import acme.entities.flight.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		int masterId = this.getRequest().getData("id", int.class);
		boolean auth = this.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		int activeAsistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Claim claim = this.repository.findClaimById(masterId);
		boolean owner = activeAsistanceAgentId == claim.getAssistanceAgent().getId();
		super.getResponse().setAuthorised(auth && owner);
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

		SelectChoices choicesLegs;
		SelectChoices choicesIssuesType;
		Collection<Leg> legs;
		Dataset dataset;

		legs = this.repository.findLegsByAirlineId(claim.getAssistanceAgent().getAirline().getId());
		choicesLegs = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		choicesIssuesType = SelectChoices.from(IssuesType.class, claim.getType());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator", "draftMode");

		dataset.put("leg", choicesLegs.getSelected().getKey());
		dataset.put("legs", choicesLegs);
		dataset.put("types", choicesIssuesType);

		super.getResponse().addData(dataset);
	}

}
