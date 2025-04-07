<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

	<acme:input-select code="technician.task-association.form.label.task" path="task" choices="${tasks}" readonly="${_command != 'create'}"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="technician.task-association.form.button.create" action="/technician/task-association/create?masterId=${masterId}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|delete') && draftMode == true}">
			<acme:submit code="technician.task-association.form.button.delete" action="/technician/task-association/delete"/>
		</jstl:when>
				
	</jstl:choose>	
</acme:form>