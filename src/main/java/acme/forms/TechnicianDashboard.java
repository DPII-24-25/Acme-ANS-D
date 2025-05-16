/*
 * TechnicianDashboard.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.maintenance_records.MaintenanceRecord;
import acme.entities.maintenance_records.MaintenanceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Map<MaintenanceStatus, Integer>	numberOfMaintenanceRecordGroupedByStatus;
	MaintenanceRecord				closestInvolvedInspectDueDate;
	List<String>					topFiveAircraftsWithMostTasks;

	Map<String, Double>				averageEstimatedCostOfMaintenanceRecordsInThelastYear;
	Map<String, Double>				minimumEstimatedCostOfMaintenanceRecordsInThelastYear;
	Map<String, Double>				maximumEstimatedCostOfMaintenanceRecordsInThelastYear;
	Map<String, Double>				standardDeviationEstimatedCostOfMaintenanceRecordsInThelastYear;

	Double							averageEstimatedDurationOfInvolvedTasks;
	Double							minimumEstimatedDurationOfInvolvedTasks;
	Double							maximumEstimatedDurationOfInvolvedTasks;
	Double							standardDeviationEstimatedDurationOfInvolvedTasks;

	/*
	 * Boolean isStddevZero1;
	 * Boolean isStddevZero2;
	 * Boolean isNoDataAvailable;
	 * Boolean isNoDataAvailableInThePastYear;
	 */
	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
