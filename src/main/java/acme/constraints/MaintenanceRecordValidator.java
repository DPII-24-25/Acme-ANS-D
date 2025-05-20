
package acme.constraints;

import java.util.Date;
import java.util.stream.Stream;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.maintenance_records.MaintenanceRecord;

@Validator
public class MaintenanceRecordValidator extends AbstractValidator<ValidMaintenaceRecord, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	//@Autowired
	//private MaintenanceRecordRepository repository;

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidMaintenaceRecord annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecord mRecord, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;
		Date moment;
		Date dueDate;
		boolean result;

		moment = mRecord.getMoment();
		dueDate = mRecord.getInspectDueDate();

		if (!(mRecord == null || dueDate == null)) {
			//super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			boolean dueDateAfterMoment;
			if (moment != null) {
				//	System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx-Moment:" + moment + "--------------DueDate:" + dueDate);
				dueDateAfterMoment = MomentHelper.isAfterOrEqual(dueDate, moment);
				super.state(context, dueDateAfterMoment, "inspectDueDate", "acme.validation.maintenance-record.inspectDueDate-is-before-moment.message");
			} else
				super.state(context, false, "inspectDueDate", "acme.validation.maintenance-record.moment-is-null-dueDate-is-not.message");
		}

		enum acceptedCurrencies {
			EUR, USD, GBP
		}

		if (mRecord.getEstCost() != null) {
			String currency = mRecord.getEstCost().getCurrency();
			boolean isAccepted = Stream.of(acceptedCurrencies.values()).anyMatch(c -> c.name().equals(currency));

			if (!isAccepted)
				super.state(context, false, "estCost", "acme.validation.maintenance-record.currency");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
