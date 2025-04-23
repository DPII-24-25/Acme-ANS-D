
package acme.features.assistance_agent.tracking_log;

import java.util.Collection;
import java.util.Comparator;
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
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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
		assert object != null;
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		assert trackingLog != null;

		/*
		 * VALIDAR LA OBLIGACION DE LA RESOLUCION SI EL ESTADO ES DIFERENTE DE PENDING
		 */

		if (trackingLog.getStatus() != null) {
			boolean condicion = trackingLog.getStatus() != TypeStatus.PENDING && trackingLog.getResolution().isEmpty();
			super.state(!condicion, "resolution", "Es obligatorio si es status es ACCEPTED o REJECTED");
		}

		// COMPROBAR SI ES  NECESARIO VERIFICAR ESTO, EN CASO SEA NECESARIO, SE PODRIA OBLLIGAR A QUE SEA MAYOR AL ULTIMO PUBLICADO

		if (trackingLog.getResolutionPorcentage() != null) {
			Collection<TrackingLog> lsTL = this.repository.findTrackingLogsByClaimId(trackingLog.getClaim().getId());
			Double maxPorcentage = lsTL.isEmpty() ? 0 : lsTL.stream().max(Comparator.comparing(TrackingLog::getResolutionPorcentage)).get().getResolutionPorcentage();
			boolean preCondicion = trackingLog.getResolutionPorcentage() == 100 && maxPorcentage == 100;
			boolean condicion = trackingLog.getResolutionPorcentage() > maxPorcentage || preCondicion;
			super.state(condicion, "resolutionPorcentage", "The resolution porcentage shoud be higher than the maximum resolution porcentage of the claim");

			// COMPRUEBO SI EXISTEN TL CON MENOR VALOR DE 100 SIN PUBLICAR, DE ESA MANERA ASEGURO QUE EL DE 100 SEA EL ULTIMO EN PUBLICAR
			boolean preCondicion2 = trackingLog.getResolutionPorcentage() == 100;
			boolean condicion2 = lsTL.stream().filter(x -> x.isDraftMode() && x.getResolutionPorcentage() < 100).count() > 0 && preCondicion2;
			super.state(!condicion2, "resolutionPorcentage", "No se puede publicar un tracking log con porcentage 100 porque existen otros sin publicar de menor valor.");
		}

		/*
		 * VALIDAR QUE UN TR CON PORCENTAGE 100 DEBA TENER ESTADO DIFERENTE DE PENDING Y VISCEVERSA
		 */

		if (trackingLog.getStatus() != null && trackingLog.getResolutionPorcentage() != null) {
			boolean condicion = trackingLog.getStatus() != TypeStatus.PENDING && trackingLog.getResolutionPorcentage() != 100;
			super.state(!condicion, "resolutionPorcentage", "The resolution porcentage should be one hundred percent");

			boolean condicion2 = trackingLog.getStatus() == TypeStatus.PENDING && trackingLog.getResolutionPorcentage() == 100;
			super.state(!condicion2, "status", "The status should be ACCEPTED or REJECTED when resolutionPorcentage is one hundred");
		}
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
