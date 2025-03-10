
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
		boolean isNull;
		boolean result;

		isNull = this.booking == null || this.booking.getCreditCard() == null;

		if (!isNull) {
			String codeCC;
			boolean lastDigits;

			codeCC = this.repository.findCreditCardByBookingId(this.booking.getId());
			lastDigits = value.equals(codeCC) || codeCC.length() == 4;
			super.state(context, lastDigits, "Cuatro digitos", "acme.validaton.booking.nibble.message");
		}
		result = !super.hasErrors(context);

		return result;
	}
}
