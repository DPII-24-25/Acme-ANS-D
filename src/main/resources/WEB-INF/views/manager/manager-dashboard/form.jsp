<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="manager.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.ranking-position"/>
		<td><acme:print value="${managerRankingPosition}"/> / <acme:print value="${totalManagersInRanking}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.years-to-retire"/>
		<td><acme:print value="${yearsUntilRetirement}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.most-popular-airport"/>
		<td><acme:print value="${mostPopularAirport}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.least-popular-airport"/>
		<td><acme:print value="${leastPopularAirport}"/></td>
	</tr>
</table>

<h3>
	<acme:print code="manager.form.title.flight-costs-eur"/>
</h3>
<table class="table table-sm">
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.average-cost"/> 
		<td><acme:print value="${averageFlightCostEUR}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.min-cost"/> 
		<td><acme:print value="${minimumFlightCostEUR}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.max-cost"/> 
		<td><acme:print value="${maximumFlightCostEUR}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.standard-deviation"/> 
		<td><acme:print value="${standardDeviationFlightCostEUR}"/></td>
	</tr>
</table>

<h3>
	<acme:print code="manager.form.title.flight-costs-gbp"/>
</h3>
<table class="table table-sm">
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.average-cost"/> 
		<td><acme:print value="${averageFlightCostGBP}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.min-cost"/> 
		<td><acme:print value="${minimumFlightCostGBP}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.max-cost"/> 
		<td><acme:print value="${maximumFlightCostGBP}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.standard-deviation"/> 
		<td><acme:print value="${standardDeviationFlightCostGBP}"/></td>
	</tr>
</table>

<h3>
	<acme:print code="manager.form.title.flight-costs-usd"/>
</h3>
<table class="table table-sm">
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.average-cost"/>
		<td><acme:print value="${averageFlightCostUSD}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.min-cost"/>
		<td><acme:print value="${minimumFlightCostUSD}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.max-cost"/>
		<td><acme:print value="${maximumFlightCostUSD}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.standard-deviation"/>
		<td><acme:print value="${standardDeviationFlightCostUSD}"/></td>
	</tr>
</table>

<h2>
	<acme:print code="manager.form.title.legs-status-ratio"/>
</h2>

<table class="table table-sm">
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.on-time-legs"/>
		<td><acme:print value="${numberOfOntimeLegs}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.delayed-legs"/>
		<td><acme:print value="${numberOfDelayedLegs}"/></td>
	</tr>
	<tr>
		<th width="80%" scope="row"><acme:print code="manager.form.label.cancelled-legs"/>
		<td><acme:print value="${numberOfCancelledLegs}"/></td>
	</tr>
	<tr>
		<th width="80%" width="60" scope="row"><acme:print code="manager.form.label.landed-legs"/>
		<td><acme:print value="${numberOfLandedLegs}"/></td>
	</tr>
</table>

