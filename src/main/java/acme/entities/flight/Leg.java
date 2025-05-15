
package acme.entities.flight;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidLeg;
import acme.entities.aircraft.Aircraft;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@ValidLeg
public class Leg extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public enum Status {
		ONTIME, DELAYED, CANCELLED, LANDED;
	};


	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{3}\\d{4}$")
	private String	flightNumber;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date	scheduleDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date	scheduleArrival;


	@Transient
	private long getDuration() {
		return (this.getScheduleArrival().getTime() - this.getScheduleDeparture().getTime()) / (1000 * 60 * 60 * 24);
	}


	@Mandatory
	@Automapped
	@Valid
	@Enumerated(EnumType.STRING)
	private Status		status;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft	aircraft;

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	private Flight		flight;

	@Mandatory
	@Automapped
	private boolean		draftMode;
}
