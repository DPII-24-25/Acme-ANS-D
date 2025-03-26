
package acme.features.assistance_agent.tracking_log;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackinglog.TrackingLog;
import acme.entities.trackinglog.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int trackingLogId;
		Claim claim;
		TrackingLog tl;

		trackingLogId = super.getRequest().getData("id", int.class);
		tl = this.repository.findTrackingLogById(trackingLogId);
		claim = tl.getClaim();

		int activeAsistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean assistanceAgentOwnsTl = tl.getClaim().getAssistanceAgent().getId() == activeAsistanceAgentId;
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status && claim != null && assistanceAgentOwnsTl && !claim.isDraftMode());
	}

	@Override
	public void load() {
		TrackingLog object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {

		SelectChoices choicesStatus;

		Dataset dataset;

		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPorcentage", "status", "resolution", "draftMode");
		dataset.put("masterId", trackingLog.getClaim().getId());
		dataset.put("statutes", choicesStatus);

		super.getResponse().addData(dataset);
	}

}
