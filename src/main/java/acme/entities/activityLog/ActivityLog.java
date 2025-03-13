
package acme.entities.activityLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidActivityLog;
import acme.entities.flight.Leg;
import acme.realms.FlightCrewMember;

@Entity
@ValidActivityLog
public class ActivityLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@ValidMoment(past = true)
	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@ValidString(max = 50)
	@Mandatory
	@Automapped
	private String				typeOfIncident;

	@ValidString(max = 255)
	@Mandatory
	@Automapped
	private String				description;

	@ValidNumber(min = 0, max = 10)
	@Mandatory
	@Automapped
	private double				severityLevel;

	@ManyToOne(optional = false)
	@Valid
	private FlightCrewMember	crewMember;

	@ManyToOne(optional = false)
	@Valid
	private Leg					leg;

}
