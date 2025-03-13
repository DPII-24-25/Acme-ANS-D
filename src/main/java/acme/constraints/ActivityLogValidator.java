
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.activityLog.ActivityLog;
import acme.entities.activityLog.ActivityLogRepository;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	@Override
	protected void initialise(final ValidActivityLog annotation) {
	}

	@Override
	public boolean isValid(final ActivityLog log, final ConstraintValidatorContext context) {
		if (log == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		return !super.hasErrors(context);
	}
}
