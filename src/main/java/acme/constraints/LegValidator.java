
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.flight.FlightRepository;
import acme.entities.flight.Leg;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result;
		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
			String airlineCode = repository.getIataCodeFromLegId(leg.getId());
			String legCode = leg.getFlightNumber().substring(0, 3);
			if (!airlineCode.equals(legCode))
				super.state(context, false, "flightNumber", "acme.validation.leg.flightNumber.message");

		}
		result = !super.hasErrors(context);
		return result;
	}

}
