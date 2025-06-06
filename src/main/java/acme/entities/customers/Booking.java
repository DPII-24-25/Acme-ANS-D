
package acme.entities.customers;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.flight.Flight;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "locatorCode")
})
public class Booking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	@Column(unique = true)

	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)

	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Automapped
	@Optional
	@ValidString(min = 4, max = 4, pattern = "^[0-9]+$")
	private String				creditCard;

	@Automapped
	@Mandatory
	@Valid
	@ManyToOne(optional = false)

	private Customer			customer;

	@Automapped
	@Mandatory
	@Valid
	@ManyToOne(optional = false)

	private Flight				flight;

	@Mandatory
	@Automapped
	private boolean				draftMode;


	@Transient
	public Money getPrice() {
		if (this.flight == null)
			return null;

		BookingRepository repo = SpringHelper.getBean(BookingRepository.class);
		Money baseCost = repo.findCostByFlightBooking(this.flight.getId());

		Collection<Passenger> passengers = repo.findAllPassengerByBookingId(this.getId());
		int passengerCount = passengers != null ? passengers.size() : 0;

		Money result = new Money();
		result.setCurrency(baseCost.getCurrency());
		result.setAmount(baseCost.getAmount() * passengerCount);

		return result;
	}

}
