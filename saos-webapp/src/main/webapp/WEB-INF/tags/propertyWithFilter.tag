<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<%@ attribute name="value" required="true" description="Property value to display" rtexprvalue="true"  type="java.lang.String" %>
<%@ attribute name="filterValue" required="false" description="Property value to display" rtexprvalue="true"  %>
<%@ attribute name="cssClass" required="false" description="Css class" rtexprvalue="true"  type="java.lang.String" %>

<c:if test="${filterValue!=null}">
	<c:set var="filterValueToShow" value="${filterValue}" scope="page"/>
</c:if>
<c:if test="${filterValue==null}">
	<c:set var="filterValueToShow" value="${value}" scope="page"/>
</c:if>

<a href="" class="${cssClass}" data-filter-value="${filterValueToShow}" data-toggle="tooltip" title="<spring:message code="judgmentSearch.filter.set" />" ><c:out value="${value}" /></a>

