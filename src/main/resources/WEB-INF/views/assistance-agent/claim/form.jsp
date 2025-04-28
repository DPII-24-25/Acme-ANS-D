<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <jstl:choose>
        <jstl:when test="${_command != 'create'}">
            <acme:input-moment code="assistance-agent.claim.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
        </jstl:when>        
    </jstl:choose>

    <acme:input-textbox code="assistance-agent.claim.form.label.passengerEmail" path="passengerEmail"/>
    <acme:input-textbox code="assistance-agent.claim.form.label.description" path="description"/>
    <acme:input-select code="assistance-agent.claim.form.label.type" path="type" choices="${types}"/>
	<jstl:choose>
        <jstl:when test="${_command != 'create'}">
			<acme:input-textbox code="assistance-agent.claim.form.label.indicator" path="indicator" readonly="true"/>
    	</jstl:when>        
    </jstl:choose>
    <acme:input-select code="assistance-agent.claim.form.label.leg" path="leg" choices="${legs}"/>

    <jstl:choose>
    	<jstl:when test="${_command == 'create'}">
            <acme:submit code="assistance-agent.claim.form.label.button.create"
                action="/assistance-agent/claim/create" />
        </jstl:when>
        <jstl:when test="${draftMode == true}">
            <acme:submit code="assistance-agent.claim.form.label.button.update"
                action="/assistance-agent/claim/update" />
            <acme:submit code="assistance-agent.claim.form.label.button.delete"
                action="/assistance-agent/claim/delete" />
            <acme:submit code="assistance-agent.claim.form.label.button.publish"
                action="/assistance-agent/claim/publish" />
        </jstl:when>
        <jstl:when test="${draftMode == false}">
            <acme:button code="assistance-agent.claim.form.label.button.trackingLogs"
                action="/assistance-agent/tracking-log/list?masterId=${id}" />                
        </jstl:when>
    </jstl:choose>
</acme:form>
