
package acme.entities.trackinglog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.claim.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				lastUpdateMoment;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				stepUndergoing;

	@Mandatory
	@ValidNumber(min = 0, max = 100)
	@Automapped
	private Long				resolutionPorcentage;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				indicator;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				resolution;

	// Relatrionships -----------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Claim				claim;

}
