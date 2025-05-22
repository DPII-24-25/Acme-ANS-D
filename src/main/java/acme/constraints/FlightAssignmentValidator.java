
package acme.constraints;

import java.util.Collection;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.flight.Leg;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.FlightAssignmentRepository;
import acme.entities.flightAssignment.FlightCrewDuty;

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
		/*
		 * if (flightAssignment.getFlightCrewMember() != null) {
		 * boolean memberAvailable = flightAssignment.getFlightCrewMember().getAvailabilityStatus().equals(FlightCrewAvailability.AVAILABLE);
		 * super.state(context, memberAvailable, "member", "{acme.validation.FlightAssignment.memberNotAvailable.message}");
		 * }
		 */
		if (flightAssignment.getLeg() != null && flightAssignment.getFlightCrewMember() != null) {

			Collection<FlightAssignment> flightAssignmentByMember;
			flightAssignmentByMember = this.repository.findFlightAssignmentByFlightCrewMemberId(flightAssignment.getFlightCrewMember().getId());

			if (!flightAssignment.getLeg().isDraftMode())
				for (FlightAssignment fa : flightAssignmentByMember)
					if (!fa.getLeg().isDraftMode() && !this.legIsCompatible(flightAssignment.getLeg(), fa.getLeg()) && fa.getId() != flightAssignment.getId()) {
						super.state(context, false, "flightCrewMember", "acme.validation.FlightAssignment.memberHasIncompatibleLegs.message");
						break;
					}
			if (flightAssignment.getDuty() != null && flightAssignment.getLeg() != null && !flightAssignment.getLeg().isDraftMode()) {

				List<FlightAssignment> flightAssignmentsByLeg;
				flightAssignmentsByLeg = this.repository.findFlightAssignmentByLegId(flightAssignment.getLeg().getId());
				boolean hasPilot = false;
				boolean hasCopilot = false;
				for (FlightAssignment fa : flightAssignmentsByLeg) {
					if (fa.getDuty().equals(FlightCrewDuty.PILOT) && flightAssignment.getId() != fa.getId())
						hasPilot = true;
					if (fa.getDuty().equals(FlightCrewDuty.CO_PILOT) && flightAssignment.getId() != fa.getId())
						hasCopilot = true;
				}

				super.state(context, !(flightAssignment.getDuty().equals(FlightCrewDuty.PILOT) && hasPilot), "duty", "{acme.validation.FlightAssignment.hasPilot.message}");
				super.state(context, !(flightAssignment.getDuty().equals(FlightCrewDuty.CO_PILOT) && hasCopilot), "duty", "{acme.validation.FlightAssignment.hasCopilot.message}");
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
