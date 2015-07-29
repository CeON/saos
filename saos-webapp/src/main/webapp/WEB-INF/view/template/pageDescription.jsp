<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<tiles:importAttribute name="metaPageDescriptionCode" />

<%-- Page description only for the root of the given page, for example for /search but not for /search?dateFrom... 
     Search engines usually omit page descriptions if they are the same for many pages 
--%>
<c:if test="${empty pageContext.request.parameterMap}"> 
	<c:if test="${!empty metaPageDescriptionCode}">
	        <meta name="description" content="<spring:message code='${metaPageDescriptionCode}'/>"/>
	</c:if>
</c:if>
    