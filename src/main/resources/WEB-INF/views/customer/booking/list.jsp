<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.booking.list.label.locatorcode" path="locatorCode" width="30%"/>
	<acme:list-column code="customer.booking.list.label.travelclass" path="travelClass" width="10%"/>
	<acme:list-column code="customer.booking.list.label.purchasemoment" path="purchaseMoment" width="10%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="customer.list.label.create" action="/customer/booking/create"/>