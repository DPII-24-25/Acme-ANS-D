
package acme.features.flight_crew_member.flight_assignment;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewAvailability;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> flightAssignments;
		int flightCrewMemberId;

		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		final Date cMoment = MomentHelper.getCurrentMoment();
		flightAssignments = this.repository.findFlightAssignmentOfPastByFlightCrewMemberId(flightCrewMemberId, cMoment);

		super.getBuffer().addData(flightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment object) {
		Dataset dataset;
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember member = this.repository.findFlightCrewMemberById(flightCrewMemberId);
		boolean available = member.getAvailabilityStatus() == FlightCrewAvailability.AVAILABLE;

		// Verifica que el estado es correcto en consola
		System.out.println("Estado del Flight Crew Member: " + available);

		dataset = super.unbindObject(object, "duty", "lastUpdate", "status", "remarks");
		dataset.put("estado", available);

		if (object.isDraftMode()) {
			final Locale local = super.getRequest().getLocale();
			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");
		} else
			dataset.put("draftMode", "No");

		super.getResponse().addData(dataset);

		// 🔹 Enviar la variable globalmente para que el JSP pueda usarla fuera de la lista
		super.getResponse().addGlobal("estado", available);
	}

}
