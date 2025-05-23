
package acme.entities.flightAssignment;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightCrewMember f where f.employeeCode =:employeeCode")
	FlightCrewMember findFlightCrewMemberByEmployeeCode(String employeeCode);

	@Query("select fa from FlightAssignment fa where fa.leg.id =:legId")
	List<FlightAssignment> findFlightAssignmentByLegId(int legId);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id =:id")
	Collection<FlightAssignment> findFlightAssignmentByFlightCrewMemberId(int id);

}
