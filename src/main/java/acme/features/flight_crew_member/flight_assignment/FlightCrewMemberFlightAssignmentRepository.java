
package acme.features.flight_crew_member.flight_assignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Leg;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id =:id")
	Collection<FlightAssignment> findFlightAssignmentByFlightCrewMemberId(int id);

	@Query("select fa from FlightAssignment fa where fa.id =:id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select l from Leg l where l.id =:id")
	Leg findLegById(int id);

	@Query("select l from Leg l where l.flight.airline.id =:id")
	Collection<Leg> findLegsByAirlineId(int id);
}
