
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Flight extends AbstractEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private Boolean				selfTransfer;

	@Mandatory
	@Automapped
	@ValidNumber
	private Double				cost;

	@Automapped
	@Optional
	@ValidString
	private String				description;

	//	Legs attributes derivative

	@Transient
	private Date				scheduleDeparture;
	@Transient
	private Date				scheduleArrivals;
	@Transient
	private String				departureCity;
	@Transient
	private String				arrivalCity;
	@Transient
	private Integer				layovers;

}
