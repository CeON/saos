<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="label" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="id" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="assignedField" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="filterValues" required="true" rtexprvalue="true"  %>

<c:if test="${!empty filterValues && filterValues != ''}" >
	<p><spring:message code="${label}" />:</p>
	<c:forEach var="value" items="${filterValues}">
		<div class="filter-item sss" id="${id}" data-assigned-field="${assignedField}" >
			<div data-tooltip-text="<spring:message code='judgmentSearch.filterBox.removeFilter' />" ><c:out value="${value}" /></div>
		</div>
	</c:forEach>
	
</c:if>	

