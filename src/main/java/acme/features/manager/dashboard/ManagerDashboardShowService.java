
package acme.features.manager.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airports.Airport;
import acme.forms.ManagerDashboard;
import acme.realms.Manager;

@GuiService
public class ManagerDashboardShowService extends AbstractGuiService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerDashboardRepository repository;

	// AbstractService<Manager, ManagerDashboard> -----------------------------


	@Override
	public void authorise() {
		boolean auth = super.getRequest().getPrincipal().hasRealmOfType(Manager.class);
		super.getResponse().setAuthorised(auth);
	}

	@Override
	public void load() {
		ManagerDashboard dashboard = new ManagerDashboard();
		int id = super.getRequest().getPrincipal().getActiveRealm().getId();

		Airport mostPopular = !this.repository.getMostPopularAirport(id).isEmpty() ? this.repository.getMostPopularAirport(id).iterator().next() : null;
		Airport leastPopular = !this.repository.getMostPopularAirport(id).isEmpty() ? this.repository.getLeastPopularAirport(id).iterator().next() : null;

		dashboard.setManagerRankingPosition(this.repository.getManagerRankingPosition(id));
		dashboard.setTotalManagersInRanking(this.repository.getTotalManagersInRanking());
		dashboard.setYearsUntilRetirement(this.repository.getYearsUntilRetirement(id));

		dashboard.setNumberOfOntimeLegs(this.repository.countOntimeLegs(id));
		dashboard.setNumberOfDelayedLegs(this.repository.countDelayedLegs(id));
		dashboard.setNumberOfCancelledLegs(this.repository.countCancelledLegs(id));
		dashboard.setNumberOfLandedLegs(this.repository.countLandedLegs(id));

		dashboard.setRatioOnTimeLegs(this.repository.getRatioOnTimeLegs(id));
		dashboard.setRatioDelayedLegs(this.repository.getRatioDelayedLegs(id));

		dashboard.setMostPopularAirport(mostPopular);
		dashboard.setLeastPopularAirport(leastPopular);

		dashboard.setAverageFlightCostEUR(this.repository.getAverageFlightCostEUR(id));
		dashboard.setMinimumFlightCostEUR(this.repository.getMinimumFlightCostEUR(id));
		dashboard.setMaximumFlightCostEUR(this.repository.getMaximumFlightCostEUR(id));
		dashboard.setStandardDeviationFlightCostEUR(this.repository.getStandardDeviationFlightCostEUR(id));

		dashboard.setAverageFlightCostGBP(this.repository.getAverageFlightCostGBP(id));
		dashboard.setMinimumFlightCostGBP(this.repository.getMinimumFlightCostGBP(id));
		dashboard.setMaximumFlightCostGBP(this.repository.getMaximumFlightCostGBP(id));
		dashboard.setStandardDeviationFlightCostGBP(this.repository.getStandardDeviationFlightCostGBP(id));

		dashboard.setAverageFlightCostUSD(this.repository.getAverageFlightCostUSD(id));
		dashboard.setMinimumFlightCostUSD(this.repository.getMinimumFlightCostUSD(id));
		dashboard.setMaximumFlightCostUSD(this.repository.getMaximumFlightCostUSD(id));
		dashboard.setStandardDeviationFlightCostUSD(this.repository.getStandardDeviationFlightCostUSD(id));

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard dashboard) {
		assert dashboard != null;

		Dataset dataset = super.unbindObject(dashboard, "managerRankingPosition", "totalManagersInRanking", "yearsUntilRetirement", "numberOfOntimeLegs", "numberOfDelayedLegs", "numberOfCancelledLegs", "numberOfLandedLegs", "ratioOnTimeLegs",
			"ratioDelayedLegs", "averageFlightCostEUR", "minimumFlightCostEUR", "maximumFlightCostEUR", "standardDeviationFlightCostEUR", "averageFlightCostGBP", "minimumFlightCostGBP", "maximumFlightCostGBP", "standardDeviationFlightCostGBP",
			"averageFlightCostUSD", "minimumFlightCostUSD", "maximumFlightCostUSD", "standardDeviationFlightCostUSD");

		dataset.put("mostPopularAirport", dashboard.getMostPopularAirport() != null ? dashboard.getMostPopularAirport().getIataCode() : null);
		dataset.put("leastPopularAirport", dashboard.getLeastPopularAirport() != null ? dashboard.getLeastPopularAirport().getIataCode() : null);

		super.getResponse().addData(dataset);
	}
}
