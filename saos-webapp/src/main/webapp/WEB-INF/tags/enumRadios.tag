<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="enumType" required="false" description="Fully qualified enum type, e.g. pl.edu.icm.saos.persistence.model.CourtType" %>
<%@ attribute name="enumItems" required="false" rtexprvalue="true" type="java.util.List" description="Enum items to show, if not defined then shows all items" %>
<%@ attribute name="id" required="false" rtexprvalue="true" description="Id prefix" %>
<%@ attribute name="path" required="false" rtexprvalue="true" description="Input radio path" %>

<c:if test="${enumItems != null}">
	<c:set var="enumItemsToShow" value="${enumItems}" scope="page"/>
</c:if>
<c:if test="${enumItems == null}">
	<spring:eval expression="T(${enumType}).values()" var="enumItemsToShow" scope="page"/>
</c:if>

<c:forEach var="enumValue" items="${enumItemsToShow}">
	<c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
	<c:set var="idLabel" value="radio-${!empty id ? id:''}${!empty id ? '-':''}${lowerCaseEnumValue}" />
	<form:radiobutton path="${path}" value="${enumValue}" id="${idLabel}" />
	<label for="${idLabel}" >
		<saos:enum value="${enumValue}" />
	</label>
</c:forEach>


