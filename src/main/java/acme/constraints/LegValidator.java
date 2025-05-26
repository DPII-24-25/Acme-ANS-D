
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.aircraft.AircraftStatus;
import acme.entities.flight.FlightRepository;
import acme.entities.flight.Leg;
import acme.entities.flight.LegRepository;

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
			LegRepository repository = SpringHelper.getBean(LegRepository.class);
			FlightRepository flightRepository = SpringHelper.getBean(FlightRepository.class);
			String airlineCode = flightRepository.getIataCodeFromFlightId(leg.getFlight().getId());

			if (!leg.getFlightNumber().isBlank() && leg.getFlightNumber().length() > 3 && leg.getFlight() != null) {
				String legCode = leg.getFlightNumber().substring(0, 3);
				if (airlineCode != null && !airlineCode.equals(legCode))
					super.state(context, false, "flightNumber", "acme.validation.leg.flightNumber.message");

				boolean isFlightNumberUnique = flightRepository.isFlightNumberUnique(leg.getFlightNumber(), leg.getId());
				super.state(context, isFlightNumberUnique, "flightNumber", "acme.validation.leg.flightNumber.unique.message");
			}

			if (leg.getScheduleArrival() != null && leg.getScheduleDeparture() != null) {
				if (leg.getScheduleArrival().before(leg.getScheduleDeparture()))
					super.state(context, false, "scheduleArrival", "acme.validation.leg.arrivalDeparture.message");
				if (leg.getFlight().getAirline().getFoundationMoment() != null)
					if (leg.getScheduleDeparture().before(leg.getFlight().getAirline().getFoundationMoment()))
						super.state(context, false, "scheduleDeparture", "acme.validation.leg.date.foundation.message");
			}

			if (leg.getArrivalAirport() != null && leg.getDepartureAirport() != null)
				if (leg.getArrivalAirport().equals(leg.getDepartureAirport()))
					super.state(context, false, "arrivalAirport", "acme.validation.leg.airports.message");

			if (leg.getStatus() != null && (leg.getStatus().equals(Leg.Status.ONTIME) || leg.getStatus().equals(Leg.Status.DELAYED)))
				if (leg.getAircraft() != null && leg.getAircraft().getStatus().equals(AircraftStatus.MAINTENANCE))
					super.state(context, false, "aircraft", "acme.validation.leg.aircraft.message");

		}
		result = !super.hasErrors(context);
		return result;
	}

}
