
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftRepository;

@Validator
public class AircraftValidator extends AbstractValidator<ValidAircraft, Aircraft> {

	@Autowired
	private AircraftRepository repository;


	@Override
	protected void initialise(final ValidAircraft annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Aircraft aircraft, final ConstraintValidatorContext context) {
		assert context != null;

		if (aircraft == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			// Aquí irían mis validaciones específicas si más adelante necesitamos:
			// - Comprobar algo contra la BD usando 'repository'
			// - Comprobar coherencia de datos, etc.
		}

		// 'hasErrors' comprueba si hemos acumulado errores durante las validaciones
		return !super.hasErrors(context);
	}

}
