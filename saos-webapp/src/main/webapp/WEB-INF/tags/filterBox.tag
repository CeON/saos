<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="enumType" required="false" description="Enum for Judgment type" %>

<spring:eval expression="T(${enumType}).values()" var="enumItemsToShow" scope="page"/>

<div class="filter-box">
	<p><spring:message code="judgment.results.filter.header" /></p>
	
	<div id="clearAllFilters"><spring:message code="judgment.results.filter.clearAll" /></div>
	
	<p><spring:message code="judgment.results.filter.court" /></p>
	<div class="filter-item" id="filter-court" assignedField="select-court" ><c:out value="${judgmentCriteriaForm.courtId}" /></div>
	
	<p><spring:message code="judgment.results.filter.division" /></p>
	<div class="filter-item" id="filter-division" assignedField="select-division" ><c:out value="${judgmentCriteriaForm.divisionId}" /></div>
	
	<div><spring:message code="judgment.results.filter.judge" /></div>
	<div class="filter-item" id="filter-judge" assignedField="input-search-judge" ><c:out value="${judgmentCriteriaForm.judgeName}" /></div>
	
	<div><spring:message code="judgment.results.filter.keywords" /></div>
	<div class="filter-item" id="filter-keyword" assignedField="input-search-keywords"><c:out value="${judgmentCriteriaForm.keyword}" /></div>
	
	<div><spring:message code="judgment.results.filter.judgmenttype" /></div>
		<c:if test="${!empty judgmentCriteriaForm.judgmentType}">
			<c:forEach var="enumValue" items="${enumItemsToShow}" varStatus="status">
				<c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
				<c:forEach items="${judgmentCriteriaForm.judgmentType}" var="judgmentType" >
					<c:if test="${judgmentType == lowerCaseEnumValue}" >
						<div class="filter-item" id="filter-judgment-type" assignedField="judgmentType${status.index + 1}">
							<spring:message code="judgment.judgmenttype.${fn:toLowerCase(judgmentType)}" />
						</div>
					</c:if>
				</c:forEach>
			</c:forEach>
		</c:if>
	
	<div><spring:message code="judgment.results.filter.date" /></div>
	
	<spring:message code="judgment.results.filter.date.from" />:<div class="filter-item" id="filter-date-from" assignedField="datepicker_from"><c:out value="${judgmentCriteriaForm.dateFrom}" /></div>
	<spring:message code="judgment.results.filter.date.to" />:<div class="filter-item" id="filter-date-to" assignedField="datepicker_to"><c:out value="${judgmentCriteriaForm.dateTo}" /></div>
		
</div>

