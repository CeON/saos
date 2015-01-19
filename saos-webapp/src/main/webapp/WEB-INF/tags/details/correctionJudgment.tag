<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="correction" required="true" rtexprvalue="true" type="pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection" %>


<c:choose>
	<c:when test="${correction.correctedProperty == 'JUDGMENT_TYPE'}" >

		<c:set var="oldValue" value="" />
		<c:forEach var="code" items="${fn:split(correction.oldValue, ', ')}" varStatus="status">
			<spring:message var="springMessage" code="pl.edu.icm.saos.persistence.model.Judgment$JudgmentType.${code}" />
			<c:choose>
				<c:when test="${status.first}" >
					<c:set var="oldValue" value="${springMessage}" />
				</c:when>
				<c:otherwise>
					<c:set var="oldValue" value="${oldValue}, ${springMessage}" />
				</c:otherwise>
			</c:choose>
		</c:forEach>
	
		<spring:message var="newValue" code="pl.edu.icm.saos.persistence.model.Judgment$JudgmentType.${correction.newValue}" />
	
		<spring:message code="judgmentDetails.corrections.judgmentType.update" arguments="${oldValue};${newValue}" 
		htmlEscape="false" argumentSeparator=";" />
	</c:when>
	<c:otherwise>
		<spring:message code="judgmentDetails.corrections.judgment.default" arguments="${correction.oldValue};${correction.newValue}" 
		htmlEscape="false" argumentSeparator=";" />
	</c:otherwise>
</c:choose>
						
