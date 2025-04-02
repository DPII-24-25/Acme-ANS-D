<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="leg.form.label.flightNumber" path="flightNumber"/>
    <acme:input-moment code="leg.form.label.scheduleDeparture" path="scheduleDeparture"/>
    <acme:input-moment code="leg.form.label.scheduleArrival" path="scheduleArrival"/>
    
    <acme:input-select code="leg.form.label.status" path="status" choices="${statusOptions}"/>
    
    <acme:input-select code="leg.form.label.flight" path="flightSelected" choices="${flightOptions}"/>
    <acme:input-select code="leg.form.label.departureAirport" path="departureAirportSelected" choices="${departureAirports}"/>
    <acme:input-select code="leg.form.label.arrivalAirport" path="arrivalAirportSelected" choices="${arrivalAirports}"/>
    <acme:input-select code="leg.form.label.aircraft" path="aircraftSelected" choices="${aircraftOptions}"/>
    
    <acme:input-checkbox code="leg.form.label.draftMode" path="draft" readonly="true"/>
    
     <acme:submit code="leg.form.label.button.update" action="/manager/leg/update"/>
            <acme:submit code="leg.form.label.button.delete" action="/manager/leg/delete"/>
            <acme:submit code="leg.form.label.button.publish" action="/manager/leg/publish"/>

    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draft == true}">
            <acme:submit code="leg.form.label.button.update" action="/manager/leg/update"/>
            <acme:submit code="leg.form.label.button.delete" action="/manager/leg/delete"/>
            <acme:submit code="leg.form.label.button.publish" action="/manager/leg/publish"/>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="leg.form.label.button.create" action="/manager/leg/create?flightId=${flightId}"/>
        </jstl:when>
    </jstl:choose>
</acme:form>