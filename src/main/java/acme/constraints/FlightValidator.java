
package acme.constraints;

import java.util.Collection;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;
import acme.entities.flight.Leg;

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

		Collection<Leg> legs = this.repository.getLegsOrderedByDeparture(flight.getId());

		// Validar que los tramos sean compatibles
		boolean incompatible = this.hasIncompatibleLegs(legs);
		super.state(context, !incompatible, "*", "acme.validation.flight.legs.departure");

		//		if (flight.getLayovers() < 0)
		//			super.state(context, false, "*", "acme.validation.flight.legs.empty");

		return !super.hasErrors(context);
	}

	public boolean hasIncompatibleLegs(final Collection<Leg> legs) {
		if (legs.size() <= 1)
			return false;

		List<Leg> legList = List.copyOf(legs);

		for (int i = 1; i < legList.size(); i++) {
			Leg previous = legList.get(i - 1);
			Leg current = legList.get(i);

			if (!current.getScheduleDeparture().after(previous.getScheduleArrival()))
				return true;

			if (previous.getDepartureAirport().getId() != current.getArrivalAirport().getId())
				return true;
		}

		return false;
	}

}
