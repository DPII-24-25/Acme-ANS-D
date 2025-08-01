
package acme.entities.flightAssignment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.flight.Leg;
import acme.realms.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(indexes = {
	// Muy usados en filtros y relaciones
	@Index(columnList = "flight_crew_member_id"), @Index(columnList = "leg_id"),
	// Para evitar escaneos completos al validar/publish
	@Index(columnList = "flight_crew_member_id, leg_id"), @Index(columnList = "flight_crew_member_id, draftMode"), @Index(columnList = "leg_id, draftMode"), @Index(columnList = "leg_id,status"),
})
@Getter
@Setter

public class FlightAssignment extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private FlightCrewDuty			duty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date					lastUpdate;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private FlightAssignmentStatus	status;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String					remarks;

	private boolean					draftMode;

	// Relatrionships -----------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightCrewMember		flightCrewMember;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg						leg;

}
