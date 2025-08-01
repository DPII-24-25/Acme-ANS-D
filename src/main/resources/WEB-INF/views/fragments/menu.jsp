<%--
- menu.jsp
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
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
		
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-juan" action="http://www.reddit.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.luigarpar1" action="https://ufcespanol.com" />
			<acme:menu-suboption code="master.menu.anonymous.ronmonalb" action="https://poki.com/es/g/level-devil" />
			<acme:menu-suboption code="master.menu.anonymous.davgavser" action="https://store.steampowered.com/app/813780/Age_of_Empires_II_Definitive_Edition/?l=spanish" />
			<acme:menu-suboption code="master.menu.anonymous.mohabu2" action="https://www.youtube.com/c/midudev" />

		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-airlines" action="/administrator/airline/list" />
			<acme:menu-suboption code="master.menu.administrator.list-airport" action="/administrator/airport/list" />
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
			<acme:menu-suboption code="master.menu.customer.list.booking" action="/customer/booking/list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.manager" access="hasRealm('Manager')">
			<acme:menu-suboption code="master.menu.manager.list.flight" action="/manager/flight/list"/>
			<acme:menu-suboption code="master.menu.manager.list.dashboard" action="/manager/manager-dashboard/show"/>
			
		</acme:menu-option>

		
		<acme:menu-option code="master.menu.technician" access="hasRealm('Technician')">
			<acme:menu-suboption code="master.menu.technician.list-maintenance-record-mine" action="/technician/maintenance-record/list?mine=true"/>
			<acme:menu-suboption code="master.menu.technician.list-maintenance-record-published" action="/technician/maintenance-record/list?mine=false"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.technician.list-task-mine" action="/technician/task/list?mine=true"/>
			<acme:menu-suboption code="master.menu.technician.list-task-published" action="/technician/task/list?mine=false"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.assistanceagent" access="hasRealm('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.assistanceagent.claim.list" action="/assistance-agent/claim/list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.flightcrewmember" access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption code="master.menu.flightcrewmember.flightassignment.listcompleted" action="/flight-crew-member/flight-assignment/list-completed"/>
			<acme:menu-suboption code="master.menu.flightcrewmember.flightassignment.listplanned" action="/flight-crew-member/flight-assignment/list-planned"/>

		</acme:menu-option>
	</acme:menu-left>

	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
		</acme:menu-option>
	</acme:menu-right>
	
</acme:menu-bar>

