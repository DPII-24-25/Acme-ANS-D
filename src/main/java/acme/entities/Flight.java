
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidFlight;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@ValidFlight
public class Flight extends AbstractEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private Boolean				selfTransfer;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				cost;

	@Automapped
	@Optional
	@ValidString
	private String				description;

	//	Legs attributes derivative

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	private Manager				manager;


	@Transient
	private Date getScheduleDeparture() {
		FligthRepository repository;
		Date wraper;

		repository = SpringHelper.getBean(FligthRepository.class);
		wraper = repository.getScheduleDeparture(this.getId());

		return wraper;
	}

	@Transient
	private Date getScheduleArrivals() {
		FligthRepository repository;
		Date wraper;

		repository = SpringHelper.getBean(FligthRepository.class);
		wraper = repository.getScheduleArrivals(this.getId());

		return wraper;

	}

	@Transient
	private String getDepartureCity() {
		FligthRepository repository;
		String wraper;

		repository = SpringHelper.getBean(FligthRepository.class);
		wraper = repository.getDepartureCity(this.getId());

		return wraper;

	}

	@Transient
	private String arrivalCity() {
		FligthRepository repository;
		String wraper;

		repository = SpringHelper.getBean(FligthRepository.class);
		wraper = repository.getArrivalCity(this.getId());

		return wraper;

	}

	@Transient
	private Integer getLayovers() {
		FligthRepository repository;
		Integer wraper;

		repository = SpringHelper.getBean(FligthRepository.class);
		wraper = repository.getLayovers(this.getId());

		return wraper;
	}

}
