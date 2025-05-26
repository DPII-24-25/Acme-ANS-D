
package acme.constraints;

import java.util.Objects;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.FlightCrewMember;
import acme.realms.FlightCrewMemberRepository;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Autowired
	private FlightCrewMemberRepository repository;


	@Override
	protected void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember crew, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (crew == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			// Validación 1: unicidad de employeeCode (excluyendo al propio registro en edición)
			int count = this.repository.countOthersByEmployeeCode(crew.getEmployeeCode(), crew.getId());
			super.state(context, count == 0, "employeeCode", "acme.validation.crew.duplicate-employeeCode");

			// Validación 2: formato del código — debe empezar con inicial del nombre y apellido
			if (crew.getEmployeeCode() != null && crew.getEmployeeCode().length() >= 2) {
				String expectedPrefix = crew.getUserAccount().getIdentity().getName().substring(0, 1) + crew.getUserAccount().getIdentity().getSurname().substring(0, 1);
				String actualPrefix = crew.getEmployeeCode().substring(0, 2);

				boolean matchingCode = Objects.equals(expectedPrefix, actualPrefix);
				super.state(context, matchingCode, "employeeCode", "acme.validation.crew.invalid-employeeCode-format");
			} else
				super.state(context, false, "employeeCode", "acme.validation.crew.invalid-employeeCode-length");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
