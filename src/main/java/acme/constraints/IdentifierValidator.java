
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.realms.Manager;

public class IdentifierValidator extends AbstractValidator<ValidIndentifier, Manager> {

	@Override
	public boolean isValid(final Manager airlineManager, final ConstraintValidatorContext context) {
		String initials = IdentifierValidator.getInitials(airlineManager.getUserAccount().getIdentity().getName(), airlineManager.getUserAccount().getIdentity().getSurname());
		String identifierNumberToValidate = airlineManager.getIdentifier().substring(0, initials.length());
		boolean result = initials.equals(identifierNumberToValidate);
		if (!result)
			super.state(context, false, "identifierNumber", "acme.validation.flight.identifierNumber.message");
		return result;
	}

	private static String getInitials(final String name, final String surname) {
		return String.valueOf(name.charAt(0)) + String.valueOf(surname.charAt(0));
	}

}
