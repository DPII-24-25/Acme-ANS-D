
package acme.entities.flightAssignment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidFlightAssignment;
import acme.entities.flight.Leg;
import acme.realms.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlightAssignment
public class FlightAssignment extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Enumerated(EnumType.STRING)
	@Mandatory
	private FlightCrewDuty			duty;

	@ValidMoment(past = true)
	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	private Date					lastUpdate;

	@Enumerated(EnumType.STRING)
	@Mandatory
	private FlightAssignmentStatus	status;

	@ValidString(max = 255)
	@Optional
	private String					remarks;

	@ManyToOne(optional = false)

	@Valid
	private FlightCrewMember		flightCrewMember;
	@ManyToOne(optional = false)

	@Valid
	private Leg						flightLeg;

}
