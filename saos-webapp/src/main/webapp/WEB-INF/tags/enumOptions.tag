<%@ tag display-name="enumOptions" description="Enum options for select with labels from message bundles. Assumed label code: EnumClass.ENUM_VALUE." small-icon="" 
	example="&lt;enumOptions enumType='pl.edu.icm.sedno.model.dict.WorkType'&gt;"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="enumType" required="false" description="fully qualified enum type, e.g. pl.edu.icm.sedno.model.dict.WorkType"%>
<%@ attribute name="enumItems" type="java.util.List" required="false" description="enum items to show, if not defined then shows all items" rtexprvalue="true"%>
<%@ attribute name="prefix" required="true" rtexprvalue="true" description="Enum checkbox name prefix" %>


<c:if test="${enumItems!=null}">
	<c:set var="enumItemsToShow" value="${enumItems}" scope="page"/>
</c:if>
<c:if test="${enumItems==null}">
	<spring:eval expression="T(${enumType}).values()" var="enumItemsToShow" scope="page"/>
</c:if>

<c:forEach var="enumValue" items="${enumItemsToShow}">
	<c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
	<option value="${lowerCaseEnumValue}" >
		<spring:message code="${prefix}.${lowerCaseEnumValue}" />
	</option>
</c:forEach>
