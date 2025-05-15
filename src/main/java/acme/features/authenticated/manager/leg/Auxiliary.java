
package acme.features.authenticated.manager.leg;

import java.util.Collection;
import java.util.List;

import acme.entities.flight.Leg;

public class Auxiliary {

	public boolean hasIncompatibleLegs(final Collection<Leg> legs, final Leg leg) {
		if (legs.size() <= 1)
			return false;

		List<Leg> legList = List.copyOf(legs);

		for (int i = 1; i < legList.size(); i++) {
			Leg previous = leg.getId() == legList.get(i - 1).getId() ? leg : legList.get(i - 1);
			Leg current = leg.getId() == legList.get(i).getId() ? leg : legList.get(i);

			if (!current.getScheduleDeparture().after(previous.getScheduleArrival()))
				return true;

			if (previous.getDepartureAirport().getId() != current.getArrivalAirport().getId())
				return true;
		}

		return false;
	}

}
