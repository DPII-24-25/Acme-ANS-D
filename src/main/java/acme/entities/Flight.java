
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidFlight;
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


	@Transient
	private Date getScheduleDeparture() {
		FlightRepository repository;
		Date wraper;

		repository = SpringHelper.getBean(FlightRepository.class);
		wraper = repository.getScheduleDeparture(this.getId());

		return wraper;
	}

	@Transient
	private Date getScheduleArrivals() {
		FlightRepository repository;
		Date wraper;

		repository = SpringHelper.getBean(FlightRepository.class);
		wraper = repository.getScheduleArrivals(this.getId());

		return wraper;

	}

	@Transient
	private String getDepartureCity() {
		FlightRepository repository;
		String wraper;

		repository = SpringHelper.getBean(FlightRepository.class);
		wraper = repository.getDepartureCity(this.getId());

		return wraper;

	}

	@Transient
	private String arrivalCity() {
		FlightRepository repository;
		String wraper;

		repository = SpringHelper.getBean(FlightRepository.class);
		wraper = repository.getArrivalCity(this.getId());

		return wraper;

	}

	@Transient
	private Integer getLayovers() {
		FlightRepository repository;
		Integer wraper;

		repository = SpringHelper.getBean(FlightRepository.class);
		wraper = repository.getLayovers(this.getId());

		return wraper;
	}

}
