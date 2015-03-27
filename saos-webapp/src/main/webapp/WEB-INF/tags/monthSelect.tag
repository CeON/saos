<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="monthSelect" description="Prints select form element with month options" %>

<%@ attribute name="id" required="true" description="Select element id" rtexprvalue="true" %>
<%@ attribute name="path" required="true" description="Select element path" rtexprvalue="true" %>
<%@ attribute name="title" required="false" description="Select element title" rtexprvalue="true" %>
        
<form:select id="${id}" path="${path}" class="form-control" title="${title}">
    <c:forEach begin="1" end="12" var="monthNumber">
        <form:option value="${monthNumber}"><spring:message code="month.${monthNumber}" text="${monthNumber}"/></form:option>
    </c:forEach>
</form:select>