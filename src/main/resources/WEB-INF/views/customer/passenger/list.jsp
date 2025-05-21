<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.label.fullName" path="fullName" width="10%"/>
	<acme:list-column code="customer.passenger.list.label.dateBirth" path="dateBirth" width="10%"/>
	<acme:list-column code="customer.passenger.list.label.specialNeeds" path="specialNeeds" width="10%"/>
	<acme:list-column code="customer.passenger.list.label.passportNumber" path="passportNumber" width="10%"/>
	<acme:list-column code="customer.passenger.list.label.email" path="email" width="10%"/>
	
	
	
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="customer.list.label.create" action="/customer/passenger/create?masterId=${masterId}"/>