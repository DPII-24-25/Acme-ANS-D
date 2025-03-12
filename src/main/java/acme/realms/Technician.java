
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
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
public class Technician extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Automapped
	private String				licenseNum;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				contactPhoneNum;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				specialisation;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				annoHealthTest;

	@Mandatory
	@ValidNumber(min = 0, max = 70)
	@Automapped
	private Integer				yearsOfExp;

	@Optional
	@ValidString(max = 50)
	@Automapped
	private String				certification;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
