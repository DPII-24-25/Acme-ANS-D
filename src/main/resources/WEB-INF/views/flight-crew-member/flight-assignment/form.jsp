<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-select code="flight-crew-member.flight-assignment.form.label.duty" path="duty" choices="${duties}"/>
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show')}">
            <acme:input-moment code="flight-crew-member.flight-assignment.form.label.lastUpdate" path="lastUpdate" readonly="true"/>
        </jstl:when>        
    </jstl:choose>
    <jstl:choose>
        <jstl:when test="${_command != 'create'}">
     <acme:input-select code="flight-crew-member.flight-assignment.form.label.status" path="status" choices="${statuts}"/>
      </jstl:when>        
    </jstl:choose>
    <acme:input-textbox code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks"/>
    <acme:input-select code="flight-crew-member.flight-assignment.form.label.leg" path="leg" choices="${legs}"/>

    <jstl:choose>
        <jstl:when test="${_command == 'show' && draftMode == true && estado}">
            <acme:submit code="flight-crew-member.flight-assignment.form.label.button.update"
                action="/flight-crew-member/flight-assignment/update" />
            <acme:submit code="flight-crew-member.flight-assignment.form.label.button.delete"
                action="/flight-crew-member/flight-assignment/delete" />
            <acme:submit code="flight-crew-member.flight-assignment.form.label.button.publish"
                action="/flight-crew-member/flight-assignment/publish" />
        </jstl:when>
        <jstl:when test="${_command == 'show' && draftMode == true}">
            <acme:submit code="flight-crew-member.flight-assignment.form.label.button.delete"
                action="/flight-crew-member/flight-assignment/delete" />
        </jstl:when>
        <jstl:when test="${_command == 'show' && draftMode == false}">
            <acme:button code="flight-crew-member.flight-assignment.form.label.button.activityLogs"
                action="/flight-crew-member/activity-log/list?masterId=${id}" />                
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="flight-crew-member.flight-assignment.form.label.button.create"
                action="/flight-crew-member/flight-assignment/create" />
        </jstl:when>
    </jstl:choose>
</acme:form>
