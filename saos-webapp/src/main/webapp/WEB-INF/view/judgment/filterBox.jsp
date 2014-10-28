<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<spring:eval expression="T(pl.edu.icm.saos.persistence.model.Judgment.JudgmentType).values()" var="enumJudgmentType" scope="page"/>


<c:if test="${showFilterBox == 'false'}" >
	<c:set var="showFilterBox" value="display-none" />
</c:if>

<div class="filter-box ${showFilterBox}" id="filter-box">
	<p><spring:message code="judgment.results.filter.header" /><span id="filter-hide" class="filter-hide"></span></p>
	
	<saos:filterField assignedField="input-search-all" label="judgment.results.filter.all" filterValue="${judgmentCriteriaForm.all}" id="filter-all"></saos:filterField>
	
	<saos:filterField assignedField="input-search-casenumber" label="judgment.results.filter.casenumber" filterValue="${judgmentCriteriaForm.signature}" id="filter-casenumber"></saos:filterField>
	
	<%-- Judgment date --%>
	<c:if test="${!empty judgmentCriteriaForm.dateFrom && judgmentCriteriaForm.dateFrom != ''}" >
		<p><spring:message code="judgment.results.filter.date.from" />:</p>
		<div class="filter-item" id="filter-date-from" data-assigned-field="datepicker_from"><c:out value="${judgmentCriteriaForm.dateFrom}" /></div>
	</c:if>
	
	<c:if test="${!empty judgmentCriteriaForm.dateTo && judgmentCriteriaForm.dateTo != ''}" >
		<p><spring:message code="judgment.results.filter.date.to" />:</p>
		<div class="filter-item" id="filter-date-to" data-assigned-field="datepicker_to">
			<div><c:out value="${judgmentCriteriaForm.dateTo}" /></div>
		</div>
	</c:if>

	
	<%-- Judgment type --%> 
	<c:if test="${!empty judgmentCriteriaForm.judgmentType}">
		<p><spring:message code="judgment.results.filter.judgmenttype" />:</p>
		<c:forEach var="enumValue" items="${enumJudgmentType}" varStatus="status">
			<c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
			<c:forEach items="${judgmentCriteriaForm.judgmentType}" var="judgmentType" >
				<c:if test="${judgmentType == lowerCaseEnumValue}" >
					<div class="filter-item" id="filter-judgment-type" data-assigned-field="judgmentType${status.index + 1}">
						<div><spring:message code="judgment.judgmenttype.${fn:toLowerCase(judgmentType)}" /></div>
					</div>
				</c:if>
			</c:forEach>
		</c:forEach>
	</c:if>
	
	<saos:filterField assignedField="input-search-judge" label="judgment.results.filter.judge" filterValue="${judgmentCriteriaForm.judgeName}" id="filter-judge"></saos:filterField>
	
	<saos:filterField assignedField="input-search-legalbases" label="judgment.results.filter.legalbases" filterValue="${judgmentCriteriaForm.legalBase}" id="filter-legal-base"></saos:filterField>
	
	<saos:filterField assignedField="input-search-referencedregulations" label="judgment.results.filter.referencedregulations" filterValue="${judgmentCriteriaForm.referencedRegulation}" id="filter-referenced-regulations"></saos:filterField>
	
	<%--Court type --%>
	<c:if test="${!empty judgmentCriteriaForm.courtType && judgmentCriteriaForm.courtType != 'all' }">
		<p><spring:message code="judgment.results.filter.courtType" />:</p>
		<c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(judgmentCriteriaForm.courtType)}" />
		<div class="filter-item" id="filter-court-type" data-assigned-field="radio-court-${lowerCaseEnumValue}">
			<div><spring:message code="judgment.results.filter.${fn:toLowerCase(lowerCaseEnumValue)}" /></div>
		</div>
	</c:if>
	
	<%@ include file="ccJudgmentFilterFields.jsp" %>
	
	<%@ include file="scJudgmentFilterFields.jsp" %>
	
	<p id="clearAllFilters" class="clear-button" ><spring:message code="judgment.results.filter.clearAll" /></p>
		
	<p id="no-filters" >
		<spring:message code="judgment.results.filter.nofilters" />
	</p>
</div>

