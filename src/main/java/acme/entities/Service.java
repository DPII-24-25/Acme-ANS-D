
package acme.entities;

import javax.persistence.Column;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;

public class Service extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				picture;

	@Mandatory
	@ValidNumber(min = 1)
	@Automapped
	private Integer				averageDwellTime;

	@Column(unique = true)
	@Optional
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$")
	private String				promotionCode;

	@Optional
	@ValidMoney
	@Automapped
	private Money				money;

}
