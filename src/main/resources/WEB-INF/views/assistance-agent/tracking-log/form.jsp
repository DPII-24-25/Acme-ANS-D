<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')}">
            <acme:input-moment code="assistance-agent.tracking-log.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
        </jstl:when>        
    </jstl:choose>

    <acme:input-textbox code="assistance-agent.tracking-log.form.label.stepUndergoing" path="stepUndergoing"/>
    <acme:input-double code="assistance-agent.tracking-log.form.label.resolutionPorcentage" path="resolutionPorcentage"/>
    <acme:input-select code="assistance-agent.tracking-log.form.label.status" path="status" choices="${statutes}"/>
    <acme:input-textbox code="assistance-agent.tracking-log.form.label.resolution" path="resolution"/>

    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
            <acme:submit code="assistance-agent.tracking-log.form.label.button.update"
                action="/assistance-agent/tracking-log/update" />
            <acme:submit code="assistance-agent.tracking-log.form.label.button.delete"
                action="/assistance-agent/tracking-log/delete" />
            <acme:submit code="assistance-agent.tracking-log.form.label.button.publish"
                action="/assistance-agent/tracking-log/publish" />
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="assistance-agent.tracking-log.form.label.button.create"
                action="/assistance-agent/tracking-log/create?masterId=${masterId}" />
        </jstl:when>
    </jstl:choose>
</acme:form>
