<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="enumType" required="false" description="fully qualified enum type, e.g. pl.edu.icm.saos.persistence.model.Judgment.JudgmentType" %>
<%@ attribute name="enumItems" required="false" rtexprvalue="true" type="java.util.List" description="enum items to show, if not defined then shows all items" %>
<%@ attribute name="path" required="false" rtexprvalue="true" description="Input checkbox name" %>

<c:if test="${enumItems!=null}">
	<c:set var="enumItemsToShow" value="${enumItems}" scope="page"/>
</c:if>
<c:if test="${enumItems==null}">
	<spring:eval expression="T(${enumType}).values()" var="enumItemsToShow" scope="page"/>
</c:if>


<c:forEach var="enumValue" items="${enumItemsToShow}">
	<c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
	<form:checkbox path="${path}" value="${lowerCaseEnumValue}" id="checkbox-${lowerCaseEnumValue}" ></form:checkbox>
	<label for="checkbox-${lowerCaseEnumValue}" >
		<saos:enum value="${enumValue}" />
	</label>
</c:forEach>
