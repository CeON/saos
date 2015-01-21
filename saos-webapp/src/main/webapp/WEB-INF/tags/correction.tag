<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="correction" required="true" rtexprvalue="true" type="pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection" %>

<spring:eval expression="T(pl.edu.icm.saos.persistence.model.Judgment)" var="JudgmentClass" ></spring:eval>
<spring:eval expression="T(pl.edu.icm.saos.persistence.model.SupremeCourtChamber)" var="ScChamberClass" ></spring:eval>
<spring:eval expression="T(pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm)" var="ScJudgmentFormClass" ></spring:eval>
<spring:eval expression="T(pl.edu.icm.saos.persistence.model.Judge)" var="JudgeClass" ></spring:eval>

<c:set var="valueSeparator" value="###!!!###" />

<div>
	<c:choose>
		<%-- Judgment --%>
		<c:when test="${JudgmentClass.isAssignableFrom(correction.correctedObjectClass)}">

			<c:choose>
				<c:when test="${correction.correctedProperty == 'JUDGMENT_TYPE'}" >
			
					<c:set var="oldValue" value="" />
					<c:forEach var="code" items="${fn:split(correction.oldValue, ', ')}" varStatus="status">
						<spring:message var="springMessage" code="judgmentDetails.corrections.value.${code}" text="${code}" />
						<c:choose>
							<c:when test="${status.first}" >
								<c:set var="oldValue" value="${springMessage}" />
							</c:when>
							<c:otherwise>
								<c:set var="oldValue" value="${oldValue}, ${springMessage}" />
							</c:otherwise>
						</c:choose>
					</c:forEach>
				
					<spring:message var="newValue" code="judgmentDetails.corrections.value.${correction.newValue}" text="${correction.newValue}" />
				
					<spring:message code="judgmentDetails.corrections.judgmentType.update" arguments="${oldValue}${valueSeparator}${newValue}" argumentSeparator="${valueSeparator}" />
				</c:when>
				<c:otherwise>
					<spring:message code="judgmentDetails.corrections.judgment.default" arguments="${correction.oldValue}${valueSeparator}${correction.newValue}" argumentSeparator="${valueSeparator}" />
				</c:otherwise>
			</c:choose>
			
		</c:when>
		
		<%-- SupremeCourtChamber --%>
		<c:when test="${correction.correctedObjectClass == ScChamberClass}">
			<c:choose>
				<c:when test="${correction.correctedProperty == 'NAME'}" >
					<spring:message code="judgmentDetails.corrections.scChamber.update" arguments="${correction.oldValue}${valueSeparator}${correction.newValue}" argumentSeparator="${valueSeparator}" />
				</c:when>
				<c:otherwise>
					<spring:message code="judgmentDetails.corrections.scChamber.default" arguments="${correction.oldValue}${valueSeparator}${correction.newValue}" argumentSeparator="${valueSeparator}" />							
				</c:otherwise>
			</c:choose>
		</c:when>
		
		<%-- SupremeCourtJudgmentForm --%>
		<c:when test="${correction.correctedObjectClass == SCJudgmentFormClass}">
			<c:choose>
				<c:when test="${correction.correctedProperty == 'NAME'}" >
					<spring:message code="judgmentDetails.corrections.scJudgmentForm.update" arguments="${correction.oldValue}${valueSeparator}${correction.newValue}" argumentSeparator="${valueSeparator}" />
				</c:when>
				<c:otherwise>
					<spring:message code="judgmentDetails.corrections.scJudgmentForm.default" arguments="${correction.oldValue}${valueSeparator}${correction.newValue}" argumentSeparator="${valueSeparator}" />
				</c:otherwise>
			</c:choose>					
		</c:when>
		
		<%-- Judge --%>
		<c:when test="${correction.correctedObjectClass == JudgeClass}">
			
			<c:choose>
				<c:when test="${correction.correctedProperty == 'NAME'}" >
					
					<c:choose>
						<c:when test="${correction.changeOperation == 'DELETE' }">
							<spring:message code="judgmentDetails.corrections.judge.delete" arguments="${correction.oldValue}"/>
						</c:when>
						<c:when test="${correction.changeOperation == 'UPDATE' }">
							<spring:message code="judgmentDetails.corrections.judge.update" arguments="${correction.oldValue}${valueSeparator}${correction.newValue}" argumentSeparator="${valueSeparator}" />
						</c:when>
						<c:otherwise>
							<spring:message code="judgmentDetails.corrections.judge.default" arguments="${correction.oldValue}${valueSeparator}${correction.newValue}" argumentSeparator="${valueSeparator}" />
						</c:otherwise>
					</c:choose>
				
				</c:when>
				<c:otherwise>
					<spring:message code="judgmentDetails.corrections.judge.default" arguments="${correction.oldValue}${valueSeparator}${correction.newValue}" argumentSeparator="${valueSeparator}" />
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<spring:message code="judgmentDetails.corrections.default" arguments="${correction.oldValue}${valueSeparator}${correction.newValue}" argumentSeparator="${valueSeparator}" />
		</c:otherwise>
		
	</c:choose>

</div>


			