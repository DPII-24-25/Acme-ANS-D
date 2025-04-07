<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show')}">
            <acme:input-moment code="flight-crew-member.activity-log.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
        </jstl:when>        
    </jstl:choose>

    <acme:input-textbox code="flight-crew-member.activity-log.form.label.typeOfIncident" path="typeOfIncident"/>
    <acme:input-double code="flight-crew-member.activity-log.form.label.description" path="description"/>
    <acme:input-textbox code="flight-crew-member.activity-log.form.label.severityLevel" path="severityLevel"/>

    <jstl:choose>
        <jstl:when test="${_command == 'show' && draftMode == true}">
            <acme:submit code="flight-crew-member.activity-log.form.label.button.update"
                action="/flight-crew-member/activity-log/update" />
            <acme:submit code="flight-crew-member.activity-log.form.label.button.delete"
                action="/flight-crew-member/activity-log/delete" />
            <acme:submit code="flight-crew-member.activity-log.form.label.button.publish"
                action="/flight-crew-member/activity-log/publish" />
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="flight-crew-member.activity-log.form.label.button.create"
                action="/flight-crew-member/activity-log/create?masterId=${masterId}" />
        </jstl:when>
    </jstl:choose>
</acme:form>
