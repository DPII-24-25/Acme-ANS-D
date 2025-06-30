/*
 * ManagerDashboard.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.forms;

import acme.client.components.basis.AbstractForm;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Manager ranking and retirement information
	Integer						managerRankingPosition;
	Integer						totalManagersInRanking;
	Integer						yearsUntilRetirement;

	Integer						numberOfOntimeLegs;
	Integer						numberOfDelayedLegs;
	Integer						numberOfCancelledLegs;
	Integer						numberOfLandedLegs;

	// Leg status statistics
	Double						ratioOnTimeLegs;
	Double						ratioDelayedLegs;

	// Airport popularity
	Airport						mostPopularAirport;
	Airport						leastPopularAirport;

	// Flight cost statistics
	Double						averageFlightCostEUR;
	Double						minimumFlightCostEUR;
	Double						maximumFlightCostEUR;
	Double						standardDeviationFlightCostEUR;

	Double						averageFlightCostGBP;
	Double						minimumFlightCostGBP;
	Double						maximumFlightCostGBP;
	Double						standardDeviationFlightCostGBP;

	Double						averageFlightCostUSD;
	Double						minimumFlightCostUSD;
	Double						maximumFlightCostUSD;
	Double						standardDeviationFlightCostUSD;

}
