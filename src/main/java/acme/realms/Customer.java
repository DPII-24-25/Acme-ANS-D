
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractRealm {

	private static final long	serialVersionUID	= 1L;

	@Automapped
	@Mandatory   // Esto puede ser que sea custom pero hay que leerse el entregable 3
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)//Hay que hacer una custom

	private String				identifier;

	@Automapped
	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")

	private String				phoneNumber;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				physicalAddress;

	@Automapped
	@Mandatory
	@ValidString(max = 50)

	private String				city;

	@Automapped
	@Mandatory
	@ValidString(max = 50)

	private String				country;

	@Automapped
	@Optional
	@ValidNumber(max = 500000, min = 0)

	private Integer				earnedPoints;

}
