<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show')}">
            <acme:input-moment code="assistance-agent.claim.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
        </jstl:when>        
    </jstl:choose>

    <acme:input-textbox code="assistance-agent.claim.form.label.passengerEmail" path="passengerEmail"/>
    <acme:input-textbox code="assistance-agent.claim.form.label.description" path="description"/>
    <acme:input-select code="assistance-agent.claim.form.label.type" path="type" choices="${types}"/>
    <acme:input-checkbox code="assistance-agent.claim.form.label.indicator" path="indicator"/>
    <acme:input-select code="assistance-agent.claim.form.label.leg" path="leg" choices="${legs}"/>

    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')}">
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
