<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="correction" required="true" rtexprvalue="true" type="pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection" %>

<spring:eval expression="T(pl.edu.icm.saos.persistence.model.CommonCourtJudgment)" var="correctionCcJudgment" ></spring:eval>
<spring:eval expression="T(pl.edu.icm.saos.persistence.model.SupremeCourtJudgment)" var="correctionScJudgment" ></spring:eval>
<spring:eval expression="T(pl.edu.icm.saos.persistence.model.SupremeCourtChamber)" var="correctionScChamber" ></spring:eval>
<spring:eval expression="T(pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm)" var="correctionSCJudgmentForm" ></spring:eval>
<spring:eval expression="T(pl.edu.icm.saos.persistence.model.Judge)" var="correctionJudge" ></spring:eval>

<div>
	<c:choose>
		<%-- CommonCourtJudgment --%>
		<c:when test="${correction.correctedObjectClass == correctionCcJudgment}">
			<saosDetails:correctionJudgment correction="${correction}" ></saosDetails:correctionJudgment>
		</c:when>
		
		<%-- SupremeCourtJudgment --%>
		<c:when test="${correction.correctedObjectClass == correctionScJudgment}">
			<saosDetails:correctionJudgment correction="${correction}" ></saosDetails:correctionJudgment>
		</c:when>
		
		<%-- SupremeCourtChamber --%>
		<c:when test="${correction.correctedObjectClass == correctionScChamber}">
			<c:choose>
				<c:when test="${correction.correctedProperty == 'NAME'}" >
					<spring:message code="judgmentDetails.corrections.scChamber.update" arguments="${correction.oldValue};${correction.newValue};" htmlEscape="false" argumentSeparator=";" />
				</c:when>
				<c:otherwise>
					<spring:message code="judgmentDetails.corrections.scChamber.default" arguments="${correction.oldValue};${correction.newValue};" htmlEscape="false" argumentSeparator=";" />							
				</c:otherwise>
			</c:choose>
		</c:when>
		
		<%-- SupremeCourtJudgmentForm --%>
		<c:when test="${correction.correctedObjectClass == correctionSCJudgmentForm}">
			<c:choose>
				<c:when test="${correction.correctedProperty == 'NAME'}" >
					<spring:message code="judgmentDetails.corrections.scJudgmentForm.update" arguments="${correction.oldValue};${correction.newValue};" htmlEscape="false" argumentSeparator=";" />
				</c:when>
				<c:otherwise>
					<spring:message code="judgmentDetails.corrections.scJudgmentForm.default" arguments="${correction.oldValue};${correction.newValue};" htmlEscape="false" argumentSeparator=";" />
				</c:otherwise>
			</c:choose>					
		</c:when>
		
		<%-- Judge --%>
		<c:when test="${correction.correctedObjectClass == correctionJudge}">
			
			<c:choose>
				<c:when test="${correction.correctedProperty == 'NAME'}" >
					
					<c:choose>
						<c:when test="${correction.changeOperation == 'DELETE' }">
							<spring:message code="judgmentDetails.corrections.judge.delete" arguments="${correction.oldValue}" htmlEscape="false" argumentSeparator=";" />
						</c:when>
						<c:when test="${correction.changeOperation == 'UPDATE' }">
							<spring:message code="judgmentDetails.corrections.judge.update" arguments="${correction.oldValue};${correction.newValue}" htmlEscape="false" argumentSeparator=";" />
						</c:when>
						<c:otherwise>
							<spring:message code="judgmentDetails.corrections.judge.default" arguments="${correction.oldValue};${correction.newValue}" htmlEscape="false" argumentSeparator=";" />
						</c:otherwise>
					</c:choose>
				
				</c:when>
				<c:otherwise>
					<spring:message code="judgmentDetails.corrections.judge.default" arguments="${correction.oldValue};${correction.newValue}" htmlEscape="false" argumentSeparator=";" />
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<spring:message code="judgmentDetails.corrections.default" arguments="${correction.oldValue};${correction.newValue}" htmlEscape="false" argumentSeparator=";" />
		</c:otherwise>
		
	</c:choose>

</div>


			