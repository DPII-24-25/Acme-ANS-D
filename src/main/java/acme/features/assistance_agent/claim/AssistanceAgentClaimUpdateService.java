
package acme.features.assistance_agent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.IssuesType;
import acme.entities.flight.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimUpdateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {

		int masterId = this.getRequest().getData("id", int.class);
		boolean auth = this.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		int activeAsistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Claim claim = this.repository.findClaimById(masterId);
		boolean owner = activeAsistanceAgentId == claim.getAssistanceAgent().getId();
		super.getResponse().setAuthorised(auth && owner && claim.isDraftMode());
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
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		super.bindObject(claim, "passengerEmail", "description", "type");
		claim.setLeg(leg);
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void validate(final Claim object) {
		assert object != null;
		final Date cMoment = MomentHelper.getCurrentMoment();
		Collection<Leg> legs = this.repository.findLegsByAirlineId(object.getAssistanceAgent().getAirline().getId(), cMoment);
		boolean correctlyLeg = legs.stream().anyMatch(x -> x.getId() == object.getLeg().getId());
		if (!correctlyLeg)
			throw new IllegalStateException("It is not possible to create a claim with this leg.");
	}

	@Override
	public void unbind(final Claim claim) {
		SelectChoices choicesLegs;
		SelectChoices choicesIssuesType;
		Collection<Leg> legs = null;
		Dataset dataset;
		final Date cMoment = MomentHelper.getCurrentMoment();
		legs = this.repository.findLegsByAirlineId(claim.getAssistanceAgent().getAirline().getId(), cMoment);
		choicesLegs = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		choicesIssuesType = SelectChoices.from(IssuesType.class, claim.getType());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type");
		dataset.put("leg", choicesLegs.getSelected().getKey());
		dataset.put("legs", choicesLegs);
		dataset.put("types", choicesIssuesType);

		super.getResponse().addData(dataset);
	}
}
