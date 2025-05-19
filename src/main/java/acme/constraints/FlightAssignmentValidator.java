
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.FlightAssignmentRepository;

@Validator
public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	protected void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignment assignment, final ConstraintValidatorContext context) {
		assert context != null;

		if (assignment == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (assignment.getLeg() != null)
			if (assignment.getLeg().getFlight() != null)
				super.state(context, !assignment.getLeg().isDraftMode() && !assignment.getLeg().getFlight().isDraft(), "leg", "javax.validation.assignment.leg");
		return !super.hasErrors(context);
	}
}
