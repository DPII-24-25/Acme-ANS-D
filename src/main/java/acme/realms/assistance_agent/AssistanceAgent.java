
package acme.realms.assistance_agent;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidAssistanceAgent;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAssistanceAgent
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

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				briefBio;

	@Mandatory
	@ValidMoney(max = 50000)
	@Automapped
	private Money				salary;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				linkPhoto;

}
