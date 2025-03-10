
package acme.constraints;

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

		if (crew == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			// Ejemplo de verificación de unicidad "manual" 

			int count = this.repository.countOthersByEmployeeCode(crew.getEmployeeCode(), crew.getId());
			if (count > 0)
				super.state(context, false, "employeeCode", "acme.error.crew.duplicate-employeeCode");

		}

		return !super.hasErrors(context);
	}

}
