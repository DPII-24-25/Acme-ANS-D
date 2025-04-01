
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
public class AssistanceAgentTrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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
		TrackingLog trackingLog;
		int trackingLogId;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog object) {
		int trackingLogId;
		Claim claim;
		TrackingLog trackingLog;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);
		claim = trackingLog.getClaim();

		super.bindObject(object, "stepUndergoing", "resolutionPorcentage", "status", "resolution");
		final Date cMoment = MomentHelper.getCurrentMoment();
		object.setLastUpdateMoment(cMoment);
		object.setClaim(claim);
	}

	@Override
	public void perform(final TrackingLog object) {
		this.repository.save(object);
	}

	@Override
	public void validate(final TrackingLog object) {
		assert object != null;
		if (object.getStatus() != TypeStatus.PENDING && object.getResolution().isEmpty())
			super.state(false, "resolution", "Es obligatorio si es status es ACCEPTED o REJECTED");
	}

	@Override
	public void unbind(final TrackingLog object) {
		SelectChoices choicesStatus;

		Dataset dataset;

		choicesStatus = SelectChoices.from(TypeStatus.class, object.getStatus());

		dataset = super.unbindObject(object, "lastUpdateMoment", "stepUndergoing", "resolutionPorcentage", "status", "resolution", "draftMode");
		dataset.put("masterId", object.getClaim().getId());
		dataset.put("statutes", choicesStatus);

		super.getResponse().addData(dataset);
	}

}
