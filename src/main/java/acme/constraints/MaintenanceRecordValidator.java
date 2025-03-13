
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.maintenance_records.MaintenanceRecord;
import acme.entities.maintenance_records.MaintenanceRecordRepository;

@Validator
public class MaintenanceRecordValidator extends AbstractValidator<ValidMaintenaceRecord, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MaintenanceRecordRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidMaintenaceRecord annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecord mRecord, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (mRecord == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean dueDateAfterMoment;
			Date moment;
			Date dueDate;

			moment = this.repository.findMomentByMR(mRecord);
			dueDate = this.repository.findDueDateByMR(mRecord);

			dueDateAfterMoment = dueDate.after(moment);

			super.state(context, dueDateAfterMoment, "inspectDueDate", "acme.validation.maintenance-record.inspectDueDate-is-before-moment.message");

		}

		result = !super.hasErrors(context);

		return result;
	}

}
