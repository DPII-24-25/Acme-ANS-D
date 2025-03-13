
package acme.entities.claim;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidClaim;
import acme.realms.assistanceAgent.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@ValidClaim
public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Enumerated(EnumType.STRING)
	@Mandatory
	private IssuesType			type;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				indicator;

	// Relatrionships -----------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent		assistanceAgent;

}
