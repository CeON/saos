<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="label" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="id" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="assignedField" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="filterValue" required="true" rtexprvalue="true"  %>

<c:if test="${!empty filterValue && filterValue != ''}" >
	<p><spring:message code="${label}" />:</p>
	<div class="filter-item" id="${id}" data-assigned-field="${assignedField}" >
		<div><c:out value="${filterValue}" /></div>
	</div>
</c:if>	
