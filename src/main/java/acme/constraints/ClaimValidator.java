
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimRepository;

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
			if (claim.isDraftMode())
				super.state(context, this.repository.findTrackingLogsByClaimId(claim.getId()).isEmpty(), "leg", "javax.validation.claim.leg4");
		}
		return !super.hasErrors(context);
	}

}
