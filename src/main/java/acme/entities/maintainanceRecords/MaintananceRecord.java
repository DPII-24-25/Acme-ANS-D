
package acme.entities.maintainanceRecords;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintananceRecord extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	private Date				moment;

	@Mandatory
	@Valid
	@Automapped
	private Status				status;

	//inspectDueDate has to be after moment
	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	private Date				inspectDueDate;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				estCost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				moreInfo;

}
