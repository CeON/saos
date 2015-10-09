<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="metaDescription" description="Prints meta description for page, when page has no request parameters." %>

<%@ attribute name="code" required="true" description="Spring message code with description" rtexprvalue="true" %>

<%-- Page description only for the root of the given page, for example for /search but not for /search?dateFrom... 
     Search engines usually omit page descriptions if they are the same for many pages 
--%>
<c:if test="${empty pageContext.request.parameterMap}"> 
    <c:if test="${!empty code}">
            <meta name="description" content="<spring:message code='${code}'/>"/>
    </c:if>
</c:if>
    