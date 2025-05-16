
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
		else if (claim.getLeg() != null)
			super.state(context, !claim.getLeg().isDraftMode(), "leg", "javax.validation.claim.leg");

		return !super.hasErrors(context);
	}

}
