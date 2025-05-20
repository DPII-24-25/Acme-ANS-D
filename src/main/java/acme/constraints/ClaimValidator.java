
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimRepository;
import acme.entities.trackinglog.TrackingLog;

@Validator
public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	@Autowired
	private ClaimRepository repository;


	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		assert context != null;

		if (claim == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (claim.getLeg() != null && claim.getLeg().getFlight() != null) {
			super.state(context, !claim.getLeg().isDraftMode() && !claim.getLeg().getFlight().isDraft(), "leg", "javax.validation.claim.leg");
			super.state(context, claim.getLeg().getFlight().getAirline().getId() == claim.getAssistanceAgent().getAirline().getId(), "leg", "javax.validation.claim.leg2");
			super.state(context, claim.getRegistrationMoment().after(claim.getLeg().getScheduleArrival()), "leg", "javax.validation.claim.leg3");
			List<TrackingLog> completos = this.repository.findTrackingLogsByClaimId(claim.getId()).stream().filter(x -> x.getResolutionPorcentage() == 100).toList();
			List<TrackingLog> noCompletos = this.repository.findTrackingLogsByClaimId(claim.getId()).stream().filter(x -> x.getResolutionPorcentage() < 100).toList();

			if (claim.isDraftMode())
				super.state(context, this.repository.findTrackingLogsByClaimId(claim.getId()).isEmpty(), "leg", "javax.validation.claim.leg4");
			else if (completos.size() > 1) {
				super.state(context, completos.stream().count() < 3, "trackingLog", "javax.validation.claim.trackingLog1");
				super.state(context, completos.stream().map(x -> x.getStatus()).distinct().count() == 1, "trackingLog", "javax.validation.claim.trackingLog2");
				super.state(context, completos.stream().filter(x -> x.isDraftMode()).count() < 2, "trackingLog", "javax.validation.claim.trackingLog3");
				super.state(context, noCompletos.stream().filter(x -> x.isDraftMode()).count() == 0, "trackingLog", "javax.validation.claim.trackingLog4");
			}
		}
		return !super.hasErrors(context);
	}

}
