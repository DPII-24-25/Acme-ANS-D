
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.trackinglog.TrackingLog;
import acme.entities.trackinglog.TrackingLogRepository;
import acme.entities.trackinglog.TypeStatus;

public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	TrackingLogRepository repository;


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		assert context != null;
		boolean isPending = false;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (trackingLog.getStatus() != null && trackingLog.getResolutionPorcentage() != null) {
			isPending = trackingLog.getStatus().equals(TypeStatus.PENDING);
			if (isPending && trackingLog.getResolutionPorcentage() == 100.00)
				super.state(context, false, "status", "The status can be “PENDING” only when the resolution percentage is not 100%");
			else if (!isPending && trackingLog.getResolutionPorcentage() != 100.00)
				super.state(context, false, "status", "The status can be “ACCEPTED” or “REJECTED” only when the resolution percentage gets to 100%");
		} else if (trackingLog.getStatus() != null && trackingLog.getResolution() != null) {
			isPending = trackingLog.getStatus().equals(TypeStatus.PENDING);
			if (!isPending && (trackingLog.getResolution() == null || trackingLog.getResolution().isBlank()))
				super.state(context, false, "status", "If the status is not “PENDING”, then the resolution is mandatory");
		} else if (trackingLog.getClaim().isDraftMode())
			super.state(context, false, "Claim", "We cannot associate a tracking log with a claim in draft mode.");
		return !super.hasErrors(context);
	}

}
