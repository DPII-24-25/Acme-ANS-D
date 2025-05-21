
package acme.features.flight_crew_member.flight_assignment;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Leg;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id =:id")
	Collection<FlightAssignment> findFlightAssignmentByFlightCrewMemberId(int id);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id =:id and fa.leg.scheduleArrival<:currentDate")
	Collection<FlightAssignment> findFlightAssignmentOfPastByFlightCrewMemberId(int id, Date currentDate);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id =:id and fa.leg.scheduleDeparture>:currentDate")
	Collection<FlightAssignment> findFlightAssignmentOfFutureByFlightCrewMemberId(int id, Date currentDate);

	@Query("select fa from FlightAssignment fa where fa.id =:id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select l from Leg l where l.id =:id")
	Leg findLegById(int id);

	@Query("select l from Leg l where l.flight.airline.id =:id")
	Collection<Leg> findLegsByAirlineId(int id);

	@Query("select l from Leg l where l.flight.airline.id =:id and l.scheduleDeparture >:scheduleDeparture")
	Collection<Leg> findLegsAfterCurrentDateByAirlineId(int id, Date scheduleDeparture);

	@Query("select a from FlightCrewMember a where a.id =:id")
	FlightCrewMember findFlightCrewMemberById(int id);

	@Query("select fa from FlightAssignment fa where fa.leg.id =:legId")
	List<FlightAssignment> findFlightAssignmentByLegId(int legId);

}
