<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="items" required="true" description="collection of case numbers to print" rtexprvalue="true" type="java.lang.Collection"%>

<c:forEach items="${items} var="caseNumber" status="statusVar">
    <c:out value="${caseNumber}${statusVar.last?'',', '}" escapeXml="false"/>
</c:forEach>


