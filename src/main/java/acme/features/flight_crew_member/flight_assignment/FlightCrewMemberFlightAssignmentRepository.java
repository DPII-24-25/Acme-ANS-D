
package acme.features.flight_crew_member.flight_assignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id =:id")
	Collection<FlightAssignment> findFlightAssignmentByFlightCrewMemberId(int id);
}
