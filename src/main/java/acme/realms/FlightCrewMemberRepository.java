
package acme.realms;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("select count(f) from FlightCrewMember f where f.employeeCode = :employeeCode and f.id != :id")
	int countOthersByEmployeeCode(String employeeCode, int id);

	@Query("select t from ActivityLog t where t.id =:id")
	ActivityLog findActivityLogById(int id);

}
