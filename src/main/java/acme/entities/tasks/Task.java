
package acme.entities.tasks;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Task extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	@Automapped
	@Enumerated(EnumType.STRING)
	private Type				type;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				deadline;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private Integer				priority;

	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private Integer				estimatedDuration;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne
	private Technician			technician;

}
