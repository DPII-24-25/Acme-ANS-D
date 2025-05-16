
package acme.features.authenticated.manager.leg;

import java.util.Collection;

import acme.entities.flight.Leg;

public class Auxiliary {

	public boolean hasOverlappingLegs(final Collection<Leg> existingLegs, final Leg legToCheck) {
		if (existingLegs == null || legToCheck == null)
			return false;

		for (Leg existingLeg : existingLegs)
			if (!existingLeg.equals(legToCheck) && this.isOverlapping(legToCheck, existingLeg))
				return true;
		return false;
	}

	private boolean isOverlapping(final Leg leg1, final Leg leg2) {
		if (leg1.getScheduleDeparture().equals(leg2.getScheduleDeparture()) && leg1.getScheduleArrival().equals(leg2.getScheduleArrival()))
			return true;

		return leg1.getScheduleDeparture().before(leg2.getScheduleArrival()) && leg1.getScheduleArrival().after(leg2.getScheduleDeparture());
	}
}
