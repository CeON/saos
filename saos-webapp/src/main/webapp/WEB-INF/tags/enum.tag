<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="enum" description="Prints localized enum" small-icon=""%>

<%@ attribute name="value" required="true" description="Enum value" rtexprvalue="true" type="java.lang.Object"%>

<c:if test="${!empty value}">
    <c:set var="enumType" value="<%=value.getClass().getName()%>"/>
    <spring:message code="${enumType}.${value}" text="${enumType}.${value}" var="enumValue"/>
    <c:out value="${enumValue}" />
</c:if>