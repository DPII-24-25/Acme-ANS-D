<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="customer.booking.form.label.flight" path="flight" choices="${publishedFlights}"/>
	<acme:input-textbox code="customer.booking.form.label.locatorcode" path="locatorCode"/>
	<acme:input-select code="customer.booking.form.label.travelclass" path="travelClass" choices="${travelClasss}"/>
	<jstl:if test="${_command != 'create'}">
		<acme:input-moment code="customer.booking.form.label.purchasemoment" path="purchaseMoment" readonly="true"/>
		<acme:input-textbox code="customer.booking.form.label.price" path="price" readonly="true"/>
	</jstl:if>

	
	<acme:input-textbox code="customer.booking.form.label.creditcard" path="creditCard"/>
	<acme:input-checkbox code="customer.booking.form.label.draftMode" path="draftMode" readonly ="true"/>
	
	

	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/passenger/list?masterId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/passenger/list?masterId=${id}"/>
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
			<acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
