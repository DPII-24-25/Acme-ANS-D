
package acme.features.assistance_agent.tracking_log;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackinglog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean owner;
		int masterId;
		int activeAsistanceAgentId;
		Claim claim;

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		activeAsistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);
		owner = claim.getAssistanceAgent().getId() == activeAsistanceAgentId;

		super.getResponse().setAuthorised(status && owner && !claim.isDraftMode());
	}

	@Override
	public void load() {
		Collection<TrackingLog> trackingLogs;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);

		trackingLogs = this.repository.findTrackingLogsByClaimId(masterId);

		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPorcentage", "status", "resolution");
		/*
		 * if (claim.isDraftMode()) {
		 * final Locale local = super.getRequest().getLocale();
		 * 
		 * dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");
		 * } else
		 * dataset.put("draftMode", "No");
		 */
		super.getResponse().addData(dataset);
	}

}
