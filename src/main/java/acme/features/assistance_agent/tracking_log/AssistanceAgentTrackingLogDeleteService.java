
package acme.features.assistance_agent.tracking_log;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackinglog.TrackingLog;
import acme.entities.trackinglog.TypeStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogDeleteService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int trackingLogId;
		Claim claim;
		TrackingLog trackingLog;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);
		claim = trackingLog.getClaim();

		int activeAsistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean assistanceAgentOwnsClaim = claim.getAssistanceAgent().getId() == activeAsistanceAgentId;
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status && claim != null && assistanceAgentOwnsClaim && !claim.isDraftMode() && trackingLog.isDraftMode());
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
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "stepUndergoing", "resolutionPorcentage", "status", "resolution");
		final Date cMoment = MomentHelper.getCurrentMoment();
		trackingLog.setLastUpdateMoment(cMoment);
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.delete(trackingLog);
	}

	@Override
	public void validate(final TrackingLog object) {
		assert object != null;
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {

		SelectChoices choicesStatus;

		Dataset dataset;

		choicesStatus = SelectChoices.from(TypeStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPorcentage", "status", "resolution", "draftMode");
		dataset.put("masterId", trackingLog.getClaim().getId());
		dataset.put("statutes", choicesStatus);

		super.getResponse().addData(dataset);
	}
}
