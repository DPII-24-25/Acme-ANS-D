
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AirManager extends AbstractRole {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Column(unique = true)
	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	private String				code;

	@Mandatory
	@ValidNumber(max = 80)
	@Automapped
	private Integer				experience;

	@Mandatory
	@ValidMoment
	@Automapped
	private Date				birthDate;

	@ValidUrl
	@Optional
	@Automapped
	private String				link;
}
