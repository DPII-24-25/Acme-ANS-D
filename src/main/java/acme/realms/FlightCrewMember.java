
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidFlightCrewMember;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlightCrewMember
public class FlightCrewMember extends AbstractRole {

	private static final long		serialVersionUID	= 1L;

	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Mandatory
	private String					employeeCode;

	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Mandatory
	@Automapped
	private String					phoneNumber;

	@ValidString(min = 1, max = 255)
	@Mandatory
	@Automapped
	private String					languageSkills;

	@Enumerated(EnumType.STRING)
	@Mandatory
	@Automapped
	private FlightCrewAvailability	availabilityStatus;

	@ManyToOne(optional = false)

	@Valid
	protected Airline				airline;

	@ValidMoney(min = 0)
	@Mandatory
	@Automapped
	private Money					salary;

	@Optional
	@ValidNumber(min = 0)
	@Automapped
	private Integer					yearsOfExperience;
}
