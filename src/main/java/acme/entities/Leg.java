
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
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
	private String	code;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date	scheduleDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date	scheduleArrival;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0)
	private Integer	duration;

	@Mandatory
	@Automapped
	@Valid
	private Status	status;

	//Ref Airport
	private String	airportArrival;
	private String	airportDeparture;

	//Ref Aircraft
	private String	aircraft;

	//Ref Flight
	@Mandatory
	@Automapped
	@ManyToOne(optional = false)
	private Flight	flight;
}
