<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistance-agent.tracking-log.list.label.lastUpdateMoment" path="lastUpdateMoment" width="20%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.stepUndergoing" path="stepUndergoing" width="20%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.resolutionPorcentage" path="resolutionPorcentage" width="20%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.status" path="status" width="20%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.resolution" path="resolution" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="assistance-agent.tracking-log.list.button.create" action="/assistance-agent/tracking-log/create"/>
