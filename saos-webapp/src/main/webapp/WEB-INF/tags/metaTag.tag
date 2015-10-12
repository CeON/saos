<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="metaTag" description="Prints meta tag, when page has no request parameters." %>

<%@ attribute name="name" required="true" description="Meta tag name, string." rtexprvalue="true" %>
<%@ attribute name="code" required="true" description="Meta tag content, spring message code." rtexprvalue="true" %>

<%-- Page meta tag only for the root of the given page, for example for /search but not for /search?dateFrom... 
     Search engines usually omit page keywords, descriptions, etc. if they are the same for many pages 
--%>
<c:if test="${empty pageContext.request.parameterMap}"> 
    <c:if test="${!empty code}">
            <meta name="${name}" content="<spring:message code='${code}'/>"/>
    </c:if>
</c:if>
    