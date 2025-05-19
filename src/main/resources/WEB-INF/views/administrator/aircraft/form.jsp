

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <!-- Registration Number -->
    <acme:input-textbox code="administrator.aircraft.form.label.registrationNumber" path="registrationNumber"/>
    
    <!-- Model -->
    <acme:input-textbox code="administrator.aircraft.form.label.model" path="model"/>
    
    <!-- Capacity -->
    <acme:input-integer code="administrator.aircraft.form.label.capacity" path="capacity" placeholder="e.g., 150"/>
    
    <!-- Cargo Weight -->
    <acme:input-double code="administrator.aircraft.form.label.cargoWeight" path="cargoWeight" placeholder="e.g., 12000.50"/>
    
    <!-- Status -->
    <acme:input-select code="administrator.aircraft.form.label.status" path="status" choices="${statusOptions}"/>
    
    <!-- Optional Details -->
    <acme:input-textarea code="administrator.aircraft.form.label.optionalDetails" path="optionalDetails"/>
    
    <!-- Airline -->
    <acme:input-select code="administrator.aircraft.form.label.airline" path="airline" choices="${airlines}"/>
    
    <!-- Confirmation checkbox -->
    <acme:input-checkbox code="administrator.aircraft.form.label.confirmation" path="confirmation"/>

    <!-- Submit buttons -->
    <jstl:choose>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="administrator.aircraft.form.button.create" action="/administrator/aircraft/create"/>    
        </jstl:when>
        <jstl:when test="${_command == 'update'}">    
            <acme:submit code="administrator.aircraft.form.button.update" action="/administrator/aircraft/update"/>
        </jstl:when>
        <jstl:when test="${_command == 'show'}">
            <acme:button code="administrator.aircraft.form.button.update" action="/administrator/aircraft/update?id=${id}"/>
        </jstl:when>                
    </jstl:choose>    
</acme:form>