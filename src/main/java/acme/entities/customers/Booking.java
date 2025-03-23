
package acme.entities.customers;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
import acme.constraints.ValidDigitsCreditCard;
import acme.entities.flight.Flight;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	@Column(unique = true)

	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)

	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Automapped
	@Mandatory
	@ValidMoney(min = 0, max = 1000000)

	private Money				price;

	@Automapped
	@Optional
	@ValidDigitsCreditCard
	private String				creditCard;

	@Automapped
	@Mandatory
	@Valid
	@ManyToOne(optional = false)

	private Customer			customer;

	@Automapped
	@Mandatory
	@Valid
	@ManyToOne(optional = false)

	private Flight				flight;

	@Mandatory
	@Automapped
	private boolean				draftMode;
}
