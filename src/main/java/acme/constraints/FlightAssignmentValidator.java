
package acme.constraints;

import java.util.Collection;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.flight.Leg;
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
	public boolean isValid(final FlightAssignment flightAssignment, final ConstraintValidatorContext context) {
		assert context != null;

		if (flightAssignment == null)
			return false;

		if (flightAssignment.getLeg() != null && flightAssignment.getFlightCrewMember() != null) {

			Collection<FlightAssignment> flightAssignmentByMember;
			flightAssignmentByMember = this.repository.findFlightAssignmentByFlightCrewMemberId(flightAssignment.getFlightCrewMember().getId());

			if (!flightAssignment.getLeg().isDraftMode())
				for (FlightAssignment fa : flightAssignmentByMember)
					if (!fa.getLeg().isDraftMode() && !this.legIsCompatible(flightAssignment.getLeg(), fa.getLeg()) && fa.getId() != flightAssignment.getId()) {
						super.state(context, false, "flightCrewMember", "acme.validation.FlightAssignment.memberHasIncompatibleLegs.message");
						break;
					}
		}
		return !super.hasErrors(context);
	}

	private boolean legIsCompatible(final Leg legToIntroduce, final Leg legDatabase) {
		boolean departureIncompatible = MomentHelper.isInRange(legToIntroduce.getScheduleDeparture(), legDatabase.getScheduleDeparture(), legDatabase.getScheduleArrival());
		boolean arrivalIncompatible = MomentHelper.isInRange(legToIntroduce.getScheduleArrival(), legDatabase.getScheduleDeparture(), legDatabase.getScheduleArrival());
		return !departureIncompatible && !arrivalIncompatible;
	}

}
