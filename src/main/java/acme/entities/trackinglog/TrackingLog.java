
package acme.entities.trackinglog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidTrackingLog;
import acme.entities.claim.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@ValidTrackingLog
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdateMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				stepUndergoing;

	@Mandatory
	@ValidScore
	@Automapped
	private Double				resolutionPorcentage;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private TypeStatus			status;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				resolution;

	private boolean				draftMode;

	// Relatrionships -----------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Claim				claim;

}
