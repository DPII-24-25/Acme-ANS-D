
package acme.features.manager.dashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airports.Airport;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	// Ranking & Retiro

	@Query("""
			SELECT COUNT(m) + 1
			FROM Manager m
			WHERE m.experience > (
				SELECT m2.experience
				FROM Manager m2
				WHERE m2.id = :managerId
			)
		""")
	Integer getManagerRankingPosition(Integer managerId);

	@Query("""
			SELECT COUNT(m)
			FROM Manager m
		""")
	Integer getTotalManagersInRanking();

	@Query("""
			SELECT 65 - YEAR(CURRENT_DATE) + YEAR(m.birthDate)
			FROM Manager m
			WHERE m.id = :managerId
		""")
	Integer getYearsUntilRetirement(Integer managerId);

	// Legs por estado

	@Query("""
			SELECT COUNT(l)
			FROM Leg l
			WHERE l.status = 'ONTIME'
			AND l.flight.airline.manager.id = :managerId
		""")
	Integer countOntimeLegs(Integer managerId);

	@Query("""
			SELECT COUNT(l)
			FROM Leg l
			WHERE l.status = 'DELAYED'
			AND l.flight.airline.manager.id = :managerId
		""")
	Integer countDelayedLegs(Integer managerId);

	@Query("""
			SELECT COUNT(l)
			FROM Leg l
			WHERE l.status = 'CANCELLED'
			AND l.flight.airline.manager.id = :managerId
		""")
	Integer countCancelledLegs(Integer managerId);

	@Query("""
			SELECT COUNT(l)
			FROM Leg l
			WHERE l.status = 'LANDED'
			AND l.flight.airline.manager.id = :managerId
		""")
	Integer countLandedLegs(Integer managerId);

	// Ratios

	@Query("""
			SELECT COUNT(l) * 1.0 / (SELECT COUNT(l1) FROM Leg l1 WHERE l1.flight.airline.manager.id = :managerId)
			FROM Leg l
			WHERE l.status = 'ONTIME'
			AND l.flight.airline.manager.id = :managerId
		""")
	Double getRatioOnTimeLegs(Integer managerId);

	@Query("""
			SELECT COUNT(l) * 1.0 / (SELECT COUNT(l1) FROM Leg l1 WHERE l1.flight.airline.manager.id = :managerId)
			FROM Leg l
			WHERE l.status = 'DELAYED'
			AND l.flight.airline.manager.id = :managerId
		""")
	Double getRatioDelayedLegs(Integer managerId);

	// Aeropuertos populares

	@Query("""
			SELECT l.departureAirport
			FROM Leg l
			WHERE l.flight.airline.manager.id = :managerId
			GROUP BY l.departureAirport
			ORDER BY COUNT(l) DESC
		""")
	Collection<Airport> getMostPopularAirport(Integer managerId);

	@Query("""
			SELECT l.departureAirport
			FROM Leg l
			WHERE l.flight.airline.manager.id = :managerId
			GROUP BY l.departureAirport
			ORDER BY COUNT(l) ASC
		""")
	Collection<Airport> getLeastPopularAirport(Integer managerId);

	// Estadísticas de coste de vuelos por moneda

	@Query("""
			SELECT AVG(f.cost.amount)
			FROM Flight f
			WHERE f.cost.currency = 'EUR'
			AND f.airline.manager.id = :managerId

		""")
	Double getAverageFlightCostEUR(Integer managerId);

	@Query("""
			SELECT MIN(f.cost.amount)
			FROM Flight f
			WHERE f.cost.currency = 'EUR'
			AND f.airline.manager.id = :managerId
		""")
	Double getMinimumFlightCostEUR(Integer managerId);

	@Query("""
			SELECT MAX(f.cost.amount)
			FROM Flight f
			WHERE f.cost.currency = 'EUR'
			AND f.airline.manager.id = :managerId
		""")
	Double getMaximumFlightCostEUR(Integer managerId);

	@Query("""
			SELECT STDDEV(f.cost.amount)
			FROM Flight f
			WHERE f.cost.currency = 'EUR'
			AND f.airline.manager.id = :managerId
		""")
	Double getStandardDeviationFlightCostEUR(Integer managerId);

	@Query("""
			SELECT AVG(f.cost.amount)
			FROM Flight f
			WHERE f.cost.currency = 'GBP'
			AND f.airline.manager.id = :managerId
		""")
	Double getAverageFlightCostGBP(Integer managerId);

	@Query("""
			SELECT MIN(f.cost.amount)
			FROM Flight f
			WHERE f.cost.currency = 'GBP'
			AND f.airline.manager.id = :managerId
		""")
	Double getMinimumFlightCostGBP(Integer managerId);

	@Query("""
			SELECT MAX(f.cost.amount)
			FROM Flight f
			WHERE f.cost.currency = 'GBP'
			AND f.airline.manager.id = :managerId
		""")
	Double getMaximumFlightCostGBP(Integer managerId);

	@Query("""
			SELECT STDDEV(f.cost.amount)
			FROM Flight f
			WHERE f.cost.currency = 'GBP'
			AND f.airline.manager.id = :managerId
		""")
	Double getStandardDeviationFlightCostGBP(Integer managerId);

	@Query("""
			SELECT AVG(f.cost.amount)
			FROM Flight f
			WHERE f.cost.currency = 'USD'
			AND f.airline.manager.id = :managerId
		""")
	Double getAverageFlightCostUSD(Integer managerId);

	@Query("""
			SELECT MIN(f.cost.amount)
			FROM Flight f
			WHERE f.cost.currency = 'USD'
			AND f.airline.manager.id = :managerId
		""")
	Double getMinimumFlightCostUSD(Integer managerId);

	@Query("""
			SELECT MAX(f.cost.amount)
			FROM Flight f
			WHERE f.cost.currency = 'USD'
			AND f.airline.manager.id = :managerId
		""")
	Double getMaximumFlightCostUSD(Integer managerId);

	@Query("""
			SELECT STDDEV(f.cost.amount)
			FROM Flight f
			WHERE f.cost.currency = 'USD'
			AND f.airline.manager.id = :managerId
		""")
	Double getStandardDeviationFlightCostUSD(Integer managerId);
}
