<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="enumType" required="false" description="Fully qualified enum type, e.g. pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType"%>
<%@ attribute name="enumItems" type="java.util.List" required="false" description="Enum items to show, if not defined then shows all items" rtexprvalue="true"%>


<c:if test="${enumItems!=null}">
	<c:set var="enumItemsToShow" value="${enumItems}" scope="page"/>
</c:if>
<c:if test="${enumItems==null}">
	<spring:eval expression="T(${enumType}).values()" var="enumItemsToShow" scope="page"/>
</c:if>

<c:forEach var="enumValue" items="${enumItemsToShow}">
	<option value="${enumValue}" >
		<saos:enum value="${enumValue}" />
	</option>
</c:forEach>
