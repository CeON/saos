<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<tiles:importAttribute name="title" />
<c:choose>
	<c:when test="${!empty title}">
		<title><spring:message code="${title}" /> - <spring:message code="saos.fullname"/></title>
	</c:when>
	<c:otherwise>
		<title><spring:message code="saos.fullname"/></title>
	</c:otherwise>
</c:choose>
