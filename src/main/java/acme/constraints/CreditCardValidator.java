
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.customers.Booking;
import acme.entities.customers.BookingRepository;

@Validator
public class CreditCardValidator extends AbstractValidator<ValidDigitsCreditCard, String> {

	private Booking				booking;
	private BookingRepository	repository;


	@Override
	protected void initialise(final ValidDigitsCreditCard annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		assert context != null;

		if (this.booking == null || this.booking.getCreditCard() == null)
			return !super.hasErrors(context);

		String codeCC = this.repository.findCreditCardByBookingId(this.booking.getId());
		boolean isValidLastDigits = value.equals(codeCC) || codeCC.length() == 4;

		super.state(context, isValidLastDigits, "Cuatro digitos", "acme.validaton.booking.nibble.message");

		return !super.hasErrors(context);
	}
}
