
package acme.constraints;

import java.util.stream.Stream;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AssistanceAgent;
import acme.realms.AssistanceAgentRepository;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Autowired
	private AssistanceAgentRepository repository;


	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent asistanceAgent, final ConstraintValidatorContext context) {
		assert context != null;

		enum acceptedCurrencies {
			EUR, USD, GBP
		}

		if (asistanceAgent == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			String currency = asistanceAgent.getSalary().getCurrency();
			boolean isAccepted = Stream.of(acceptedCurrencies.values()).anyMatch(c -> c.name().equals(currency));
			super.state(context, isAccepted, "cost", "acme.validation.assistanceAgent.currency");
		}

		return !super.hasErrors(context);
	}

}
