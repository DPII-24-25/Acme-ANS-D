
package acme.entities.claim;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidClaim;
import acme.entities.flight.Leg;
import acme.entities.trackinglog.TypeStatus;
import acme.realms.AssistanceAgent;
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
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private IssuesType			type;

	private boolean				draftMode;

	// Relatrionships -----------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent		assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;

	// Atributos derivados ------------------------------------------------


	@Transient
	public TypeStatus getIndicator() {
		ClaimRepository repository = SpringHelper.getBean(ClaimRepository.class);
		Collection<TypeStatus> statusTL = repository.findTrackingLogStatusByClaimId(this.getId());
		// ESTO SE DEBERA MODIFICAR EN CASO SE PERMITA QUE LOS TL DE 100 PUEDAN TENER DIFERENTE ESTATUS
		// PUEDO COGER EN CASO SOLO EXISTA UN VALOR DE 100 COJO SU STATUS Y SI HAY MAS COGO EL QUE TENGA LA FECHA MAS RECIENTE
		if (statusTL.contains(TypeStatus.ACCEPTED))
			return TypeStatus.ACCEPTED;
		if (statusTL.contains(TypeStatus.REJECTED))
			return TypeStatus.REJECTED;
		return TypeStatus.PENDING;
	}

}
