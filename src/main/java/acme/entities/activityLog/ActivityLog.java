
package acme.entities.activityLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
import acme.entities.flightAssignment.FlightAssignment;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(indexes = {
	@Index(columnList = "flight_assignment_id"), @Index(columnList = "flight_assignment_id, draftMode"), @Index(columnList = "registrationMoment"),
})
@Getter
@Setter
@ValidActivityLog
public class ActivityLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				typeOfIncident;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private double				severityLevel;

	private boolean				draftMode;

	// Relatrionships -----------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightAssignment	flightAssignment;

}
