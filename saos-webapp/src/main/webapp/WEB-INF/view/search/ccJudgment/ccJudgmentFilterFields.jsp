<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<saos:filterField assignedField="select-common-court" label="judgmentSearch.formField.commonCourt" filterValue="${judgmentCriteriaForm.commonCourtId}" id="filter-court"></saos:filterField>
	
<saos:filterField assignedField="select-common-division" label="judgmentSearch.formField.commonDivision" filterValue="${judgmentCriteriaForm.commonCourtDivisionId}" id="filter-division"></saos:filterField>

 	 
<c:if test="${!empty judgmentCriteriaForm.keywords && judgmentCriteriaForm.keywords != ''}" >
	<p><spring:message code="judgmentSearch.formField.keywords" />:</p>
	<c:forEach items="${judgmentCriteriaForm.keywords}" var="keyword">
		<div class="filter-item" id="filter-keyword" data-assigned-field="input-search-keywords-cc" >
			<div data-tooltip-text="<spring:message code='judgmentSearch.filterBox.removeFilter' />" >
				<c:out value="${keyword}" />		
			</div>
		</div>
	</c:forEach>
</c:if>	

		