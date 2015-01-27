<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="enumType" required="false" description="fully qualified enum type, e.g. pl.edu.icm.saos.persistence.model.Judgment.JudgmentType" %>
<%@ attribute name="enumItems" required="false" rtexprvalue="true" type="java.util.List" description="enum items to show, if not defined then shows all items" %>
<%@ attribute name="path" required="false" rtexprvalue="true" description="Input checkbox name" %>
<%@ attribute name="columnsNumber" required="false" rtexprvalue="true" description="Number of columns in which checkboxes are being displayed. It can take values 1, 2 or 3." %>

<c:if test="${enumItems!=null}">
	<c:set var="enumItemsToShow" value="${enumItems}" scope="page"/>
</c:if>
<c:if test="${enumItems==null}">
	<spring:eval expression="T(${enumType}).values()" var="enumItemsToShow" scope="page"/>
</c:if>
<c:if test="${empty columnsNumber}" >
	<c:set var="columnsNumber" value="1" />
</c:if>
<c:if test="${columnsNumber > 3}" >
		<c:set var="columnsNumber" value="3" />
</c:if>

<c:forEach var="enumValue" items="${enumItemsToShow}">
	<c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
	
	<div class="col-xs-<fmt:formatNumber value="${12 / columnsNumber}" maxFractionDigits="0"/>">
		<form:checkbox path="${path}" value="${enumValue}" id="checkbox-${lowerCaseEnumValue}" ></form:checkbox>
		<label for="checkbox-${lowerCaseEnumValue}" >
			<saos:enum value="${enumValue}" />
		</label>
	</div>
</c:forEach>

