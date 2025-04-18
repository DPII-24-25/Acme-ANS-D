<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	
	<acme:list-column code="technician.task-association.list.label.task" path="task" width="100%"/>

</acme:list>

<jstl:choose>	
	<jstl:when test="${draftMode == true}">
		<acme:button code="technician.task-association.list.button.create" action="/technician/task-association/create?masterId=${masterId}"/>
	</jstl:when> 		
</jstl:choose>	