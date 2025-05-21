
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.activityLog.ActivityLog;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog log, final ConstraintValidatorContext context) {
		assert context != null;

		if (log == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		boolean valid = true;

		// Validar estructura: FlightAssignment, Leg y ScheduledArrival no pueden ser nulos
		boolean structureOk = log.getFlightAssignment() != null && log.getFlightAssignment().getLeg() != null && log.getFlightAssignment().getLeg().getScheduleArrival() != null;

		super.state(context, structureOk, "*", "javax.validation.constraints.NotNull.message");
		valid &= structureOk;

		// Validar que registrationMoment no sea null
		boolean hasRegistrationMoment = log.getRegistrationMoment() != null;
		super.state(context, hasRegistrationMoment, "registrationMoment", "acme.validation.activitylog.registrationmoment.required");
		valid &= hasRegistrationMoment;

		/*
		 * // Validar que registrationMoment sea >= scheduledArrival
		 * if (structureOk && hasRegistrationMoment) {
		 * Date scheduledArrival = log.getFlightAssignment().getLeg().getScheduleArrival();
		 * Date registrationMoment = log.getRegistrationMoment();
		 * 
		 * boolean isAfterOrEqual = MomentHelper.isAfterOrEqual(registrationMoment, scheduledArrival);
		 * super.state(context, isAfterOrEqual, "registrationMoment", "acme.validation.activitylog.registrationmoment.tooEarly");
		 * valid &= isAfterOrEqual;
		 * }
		 */
		return valid && !super.hasErrors(context);
	}
}
