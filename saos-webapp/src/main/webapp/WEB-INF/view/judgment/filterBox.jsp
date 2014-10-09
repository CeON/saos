<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<spring:eval expression="T(pl.edu.icm.saos.persistence.model.Judgment.JudgmentType).values()" var="enumJudgmentType" scope="page"/>

<div class="filter-box">
	<p><spring:message code="judgment.results.filter.header" /></p>
	
	<p id="clearAllFilters" class="clear-button" ><spring:message code="judgment.results.filter.clearAll" /></p>
	
	<saos:filterField assignedField="select-court" label="judgment.results.filter.court" filterValue="${judgmentCriteriaForm.courtId}" id="filter-court"></saos:filterField>
	
	<saos:filterField assignedField="select-division" label="judgment.results.filter.division" filterValue="${judgmentCriteriaForm.divisionId}" id="filter-division"></saos:filterField>

	<saos:filterField assignedField="input-search-judge" label="judgment.results.filter.judge" filterValue="${judgmentCriteriaForm.judgeName}" id="filter-judge"></saos:filterField>
	
	<saos:filterField assignedField="input-search-keywords" label="judgment.results.filter.keywords" filterValue="${judgmentCriteriaForm.keyword}" id="filter-keyword"></saos:filterField>
	 
	<p><spring:message code="judgment.results.filter.judgmenttype" />:</p>
		<c:if test="${!empty judgmentCriteriaForm.judgmentType}">
			<c:forEach var="enumValue" items="${enumJudgmentType}" varStatus="status">
				<c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
				<c:forEach items="${judgmentCriteriaForm.judgmentType}" var="judgmentType" >
					<c:if test="${judgmentType == lowerCaseEnumValue}" >
						<div class="filter-item" id="filter-judgment-type" data-assigned-field="judgmentType${status.index + 1}">
							<spring:message code="judgment.judgmenttype.${fn:toLowerCase(judgmentType)}" />
						</div>
					</c:if>
				</c:forEach>
			</c:forEach>
		</c:if>
	
	<p><spring:message code="judgment.results.filter.date" />:</p>
	
	<c:if test="${!empty judgmentCriteriaForm.dateFrom && judgmentCriteriaForm.dateFrom !=  ''}" >
		<spring:message code="judgment.results.filter.date.from" />:<div class="filter-item" id="filter-date-from" data-assigned-field="datepicker_from"><c:out value="${judgmentCriteriaForm.dateFrom}" /></div>
	</c:if>
	
	<c:if test="${!empty judgmentCriteriaForm.dateTo && judgmentCriteriaForm.dateTo !=  ''}" >
		<spring:message code="judgment.results.filter.date.to" />:<div class="filter-item" id="filter-date-to" data-assigned-field="datepicker_to"><c:out value="${judgmentCriteriaForm.dateTo}" /></div>
	</c:if>
		
</div>

