<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="flight.form.label.tag" path="tag" />
	<acme:input-checkbox code="flight.form.label.selfTransfer"
		path="selfTransfer" />
	<acme:input-textarea code="flight.form.label.description"
		path="description" />
	<acme:input-select code="flight.form.label.airline" path="airline"
		choices="${airlines}" />
	<acme:input-money code="flight.form.label.cost" path="cost" />



	<acme:input-checkbox code="flight.form.label.draftMode" path="draft"
		readonly="true" />
	<jstl:if test="${acme:anyOf(_command, 'show|update|publish|delete')}">
		<acme:input-moment code="flight.form.label.scheduleArrival"
			path="scheduleArrival" readonly="true" />
		<acme:input-moment code="flight.form.label.scheduleDeparture"
			path="scheduleDeparture" readonly="true" />
		<acme:input-textbox code="flight.form.label.arrivalCity"
			path="arrivalCity" readonly="true" />
		<acme:input-textbox code="flight.form.label.departureCity"
			path="departureCity" readonly="true" />
		<acme:input-textbox code="flight.form.label.layovers" path="layovers"
			readonly="true" />
	</jstl:if>


	<jstl:choose>
		<jstl:when
			test="${acme:anyOf(_command, 'show|update|delete|publish') && draft == true}">
			<acme:submit code="flight.form.label.button.update"
				action="/manager/flight/update" />
			<acme:submit code="flight.form.label.button.delete"
				action="/manager/flight/delete" />
			<acme:submit code="flight.form.label.button.publish"
				action="/manager/flight/publish" />

			<acme:button code="flight.form.label.button.legs" action="/manager/leg/list?flightId=${id}" />
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight.form.label.button.create"
				action="/manager/flight/create" />
		</jstl:when>
	</jstl:choose>


</acme:form>
