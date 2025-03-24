<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="assistance-agent.claim.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.passengerEmail" path="passengerEmail" readonly="true"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.description" path="description" readonly="true"/>
	<acme:input-select code="assistance-agent.claim.form.label.type" path="type" choices="${types}"/>
	<acme:input-checkbox code="assistance-agent.claim.form.label.indicator" path="indicator" readonly="true"/>
	<acme:input-select code="assistance-agent.claim.form.label.flight" path="flight" choices="${flights}"/>
	
	<jstl:choose>
		<jstl:when
			test="${acme:anyOf(_command, 'show|update|delete|publish')}">
			<acme:submit code="assistance-agent.claim.form.label.button.update"
				action="/assistance-agent/claim/update" />
			<acme:submit code="assistance-agent.claim.form.label.button.delete"
				action="/assistance-agent/claim/delete" />
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.claim.form.label.button.create"
				action="/assistance-agent/claim/create" />
		</jstl:when>
	</jstl:choose>


</acme:form>