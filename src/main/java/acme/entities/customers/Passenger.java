
package acme.entities.customers;

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
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passenger extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;
	@Automapped
	@Mandatory
	@ValidString(max = 255)

	private String				fullName;

	@Automapped
	@Mandatory
	@ValidEmail

	private String				email;

	@Automapped
	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	@Column(unique = true)
	private String				passportNumber;

	@Automapped
	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateBirth;

	@Automapped
	@Optional
	@ValidString(max = 50)
	private String				specialNeeds;

	@Automapped
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Booking				booking;
}
