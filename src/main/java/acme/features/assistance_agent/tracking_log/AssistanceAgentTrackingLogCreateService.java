
package acme.features.assistance_agent.tracking_log;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);

		int activeAsistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean assistanceAgentOwnsClaim = claim.getAssistanceAgent().getId() == activeAsistanceAgentId;
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status && claim != null && assistanceAgentOwnsClaim && !claim.isDraftMode());
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		Claim claim;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);
		trackingLog = new TrackingLog();
		trackingLog.setDraftMode(true);
		trackingLog.setClaim(claim);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "stepUndergoing", "resolutionPorcentage", "status", "resolution");
		final Date cMoment = MomentHelper.getCurrentMoment();
		trackingLog.setLastUpdateMoment(cMoment);
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		assert trackingLog != null;
		Collection<TrackingLog> lsTL = this.repository.findTrackingLogsByClaimId(trackingLog.getClaim().getId());
		List<TrackingLog> lsTL100 = lsTL.stream().filter(x -> x.getResolutionPorcentage() == 100).toList();

		/*
		 * VALIDACION DE SI SE PERMITE CREAR O NO UN TRACKING LOG, EN CASO NO EXISTA EL DE 100 O PARA EL 100 EXTRA
		 */

		boolean availableToCreate = false;
		if (lsTL100.size() == 1)
			availableToCreate = !lsTL100.get(0).isDraftMode();  // Si existe uno de 100 y es editable no se debería poder crear más, solo se creará el extra una vez el de 100 se halla publciado.
		else if (lsTL100.isEmpty())
			availableToCreate = true; // Si no hay ninguno de 100 se debe permitir seguir creando

		if (availableToCreate)
			throw new IllegalStateException("It is not possible to create more trackinglog for this claim.");

		/*
		 * VALIDAR LA OBLIGACION DE LA RESOLUCION SI EL ESTADO ES DIFERENTE DE PENDING
		 */

		if (trackingLog.getStatus() != null) {
			boolean condicion = trackingLog.getStatus() != TypeStatus.PENDING && trackingLog.getResolution().isEmpty();
			super.state(!condicion, "resolution", "Es obligatorio si es status es ACCEPTED o REJECTED");
		}

		/*
		 * VALIDAR QUE EL PORCENTAGE DEBA SER SUPERIOR A LOS DEMAS
		 */

		if (trackingLog.getResolutionPorcentage() != null) {
			Double maxPorcentage = lsTL.isEmpty() ? 0 : lsTL.stream().max(Comparator.comparing(TrackingLog::getResolutionPorcentage)).get().getResolutionPorcentage();
			boolean preCondicion = trackingLog.getResolutionPorcentage() == 100 && maxPorcentage == 100;
			boolean condicion = trackingLog.getResolutionPorcentage() > maxPorcentage || preCondicion;
			super.state(condicion, "resolutionPorcentage", "The resolution porcentage shoud be higher than the maximum resolution porcentage of the claim");
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
