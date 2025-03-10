
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidAircraft;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAircraft
public class Aircraft extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@Column(unique = true)
	@ValidString(max = 50)
	@Mandatory
	protected String			registrationNumber;

	@ValidString(max = 50)
	@Automapped
	@Mandatory
	protected String			model;

	@ValidNumber(min = 1)
	@Automapped
	@Mandatory
	protected int				capacity;

	@ValidNumber(min = 2000, max = 50000)
	@Automapped
	@Mandatory
	protected double			cargoWeight;

	@Enumerated(EnumType.STRING)
	@Automapped
	@Mandatory
	protected AircraftStatus	status;

	@ValidString(max = 255)
	@Automapped
	@Optional
	protected String			optionalDetails;

	/*
	 * @ManyToOne(optional = false)
	 * 
	 * @Automapped
	 * 
	 * @Valid
	 * protected Airline airline;
	 */

}
