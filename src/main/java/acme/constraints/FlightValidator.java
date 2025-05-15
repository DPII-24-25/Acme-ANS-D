
package acme.constraints;

import java.util.stream.Stream;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	@Autowired
	private FlightRepository repository;


	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		assert context != null;

		if (flight == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		enum acceptedCurrencies {
			EUR, USD, GBP
		}

		if (flight.getCost() != null) {
			String currency = flight.getCost().getCurrency();
			boolean isAccepted = Stream.of(acceptedCurrencies.values()).anyMatch(c -> c.name().equals(currency));

			if (!isAccepted)
				super.state(context, false, "cost", "acme.validation.flight.currency");
		}
		return !super.hasErrors(context);
	}

}
