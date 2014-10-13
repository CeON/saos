<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="enumType" required="false" description="fully qualified enum type, e.g. pl.edu.icm.saos.webapp.judgment.CourtType" %>
<%@ attribute name="enumItems" required="false" rtexprvalue="true" type="java.util.List" description="enum items to show, if not defined then shows all items" %>
<%@ attribute name="prefix" required="true" rtexprvalue="true" description="Enum checkbox name prefix" %>
<%@ attribute name="id" required="false" rtexprvalue="true" description="Id prefix" %>
<%@ attribute name="path" required="false" rtexprvalue="true" description="Checkbox name" %>

<c:if test="${enumItems!=null}">
	<c:set var="enumItemsToShow" value="${enumItems}" scope="page"/>
</c:if>
<c:if test="${enumItems==null}">
	<spring:eval expression="T(${enumType}).values()" var="enumItemsToShow" scope="page"/>
</c:if>


<c:forEach var="enumValue" items="${enumItemsToShow}">
	<c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
	<c:set var="idLabel" value="radio-${!empty id ? id:''}${!empty id ? '-':''}${lowerCaseEnumValue}" />
	<form:radiobutton path="${path}" value="${lowerCaseEnumValue}" id="${idLabel}" />
	<label for="${idLabel}" ><spring:message code="${prefix}.${lowerCaseEnumValue}" /></label>
</c:forEach>
