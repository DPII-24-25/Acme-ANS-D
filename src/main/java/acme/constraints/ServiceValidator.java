
package acme.constraints;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.components.ServiceRepository;
import acme.entities.service.Service;

@Validator
public class ServiceValidator extends AbstractValidator<ValidService, Service> {

	@Autowired
	private ServiceRepository repository;


	@Override
	protected void initialise(final ValidService annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result = false;

		if (service == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Date fechaActual = new Date();
			SimpleDateFormat formato = new SimpleDateFormat("yy");
			String ultimosDosNumerosDelAnyo = formato.format(fechaActual);
			String ultimosDosDigitosDePromotionCode = service.getPromoCode().substring(service.getPromoCode().length() - 2);
			super.state(context, ultimosDosDigitosDePromotionCode.equals(ultimosDosNumerosDelAnyo), "promotionalCode", "acme.validation.service.promotionalCode.message");
		}

		return !super.hasErrors(context);
	}

}
