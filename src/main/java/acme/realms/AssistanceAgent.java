
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssistanceAgent extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Column(unique = true)
	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	private String				employeeCode;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				spokenLanguages;

	/*
	 * @Mandatory
	 * 
	 * @Valid
	 * 
	 * @Automapped
	 * private Airline airline;
	 */

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				moment;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				briefBio;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				linkPhoto;

}
