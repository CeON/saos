<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<script>
$(document).ready(function() {
	jsInitInJudgmentDetails({
		pageTitle: `<saos:caseNumber items='${judgment.caseNumbers}'/>` 
	});
});
</script>

<div class="container judgment-page block" id="judgment">

	<c:if test="${!empty judgment.judgmentType}" >
		<div class="judgment-type">
			<spring:message code="judgment.${fn:toLowerCase(judgment.judgmentType)}" />
		</div>
	</c:if> 
	
	<c:if test="${!empty judgment.caseNumbers}" >
		<h2>		
			<saos:caseNumber items="${judgment.caseNumbers}"/>
		</h2>
	</c:if>
	
	<div class="col-md-12" >
	
		<ul class="judgment-data">
		
			<c:if test="${!empty judgment.judgmentDate}" >
				<li>
					<div class="" >
						<div class="label-title" ><spring:message code="judgment.date" />:</div>
						<div class="desc" ><joda:format value="${judgment.judgmentDate}" pattern="${DATE_PATTERN}"/></div>
					</div>
				</li>
			</c:if>
			
			
			<%-- CommonCourtJudgment properties --%>
			<c:if test="${judgment.courtType == 'COMMON'}" >
			
				<c:if test="${!empty judgment.courtDivision.court.name}" >
					<li>
						<div class="" >
							<div class="label-title" ><spring:message code="judgment.commonCourt" />:</div>
							<div class="desc" >
								<c:out value="${judgment.courtDivision.court.name}" />
							</div>
						</div>
					</li>
				</c:if>
				
				<c:if test="${!empty judgment.courtDivision.name}" >
					<li>
						<div class="" >
							<div class="label-title" ><spring:message code="judgment.commonDivision" />:</div>
							<div class="desc" >
								<c:out value="${judgment.courtDivision.name}" />
							</div>
						</div>
					</li>
				</c:if>
			
			</c:if>
			
			
			<%-- SupremeCourtJudgment properties --%>
			<c:if test="${judgment.courtType == 'SUPREME'}" >
			
				<c:if test="${!empty judgment.scChambers}" >
					<li>
						<div class="" >
							<div class="label-title" ><spring:message code="judgment.supremeChambers" />:</div>
							<div class="desc" >
								<c:forEach items="${judgment.scChambers}" var="chamber">
									<c:out value="${chamber.name}" />
								</c:forEach>	
							</div>
						</div>
					</li>
				</c:if>
				
				<c:if test="${!empty judgment.scChamberDivision.name}" >
					<li>
						<div class="" >
							<div class="label-title" ><spring:message code="judgment.supremeChamberDivisionFullName" />:</div>
							<div class="desc" >
								<c:out value="${judgment.scChamberDivision.scChamber.name}" /> 
								<c:out value="${judgment.scChamberDivision.name}" />
							</div>
						</div>
					</li>
				</c:if>
				
				<c:if test="${!empty judgment.scJudgmentForm}" >
					<li>
						<div class="" >
							<div class="label-title" ><spring:message code="judgment.scJudgmentForm" />:</div>
							<div class="desc" >
								<c:out value="${judgment.scJudgmentForm.name}" />
							</div>
						</div>
					</li>
				</c:if>
				
				<c:if test="${!empty judgment.personnelType}" >
					<li>
						<div class="" >
							<div class="label-title" ><spring:message code="judgment.personnelType" />:</div>
							<div class="desc" >
								<saos:enum value="${judgment.personnelType}" />
							</div>
						</div>
					</li>
				</c:if>
			
			</c:if>
			
			<c:if test="${!empty judgment.judges}" >
				<li>
					<div class="" >
						<div class="label-title" ><spring:message code="judgment.judges" />:</div>
						<div class="desc" >
							<c:forEach items="${judgment.judges}" var="judge" >
								<p>
									<c:out value="${judge.name}" />
									<c:if test="${judge.presidingJudge}" >
										<span class="presiding-judge"  data-toggle="tooltip" title="<spring:message code="judgment.judgeRole.PRESIDING_JUDGE" />" >
											<img src="${contextPath}/static/image/icons/judge.png" alt="<spring:message code="judgment.judgeRole.PRESIDING_JUDGE.iconAlt" />" />
										</span>
									</c:if>
								</p>
							</c:forEach>
						</div>
					</div>
				</li>
			</c:if>
			
			<c:if test="${!empty judgment.courtReporters}" >
				<li>
					<div class="" >
						<div class="label-title" ><spring:message code="judgment.reporters" />:</div>
						<div class="desc" >
							<c:forEach items="${judgment.courtReporters}" var="reporter" >
								<p><c:out value="${reporter}" /></p>
							</c:forEach>
						</div>
					</div>
				</li>
			</c:if>
			
		</ul>
	
	
		<c:if test="${!empty judgment.keywords}" >
			<h3><spring:message code="judgment.keywords" />:</h3>
			<div class="keywords">	
				<c:forEach items="${judgment.keywords}" var="keyword" >
					<div class="keyword"><c:out value="${keyword.phrase}" /></div>
				</c:forEach>
			</div>
		</c:if>
		
			
		<c:if test="${!empty judgment.legalBases}" >
			<h3><spring:message code="judgment.legalBases" />:</h3>
			<div class="legalBases">	
				<c:forEach items="${judgment.legalBases}" var="legalBase" >
					<div class="legalBase"><c:out value="${legalBase}" /></div>
				</c:forEach>
			</div>
		</c:if>
		
		<c:if test="${!empty judgment.referencedRegulations}" >
			<h3><spring:message code="judgment.referencedRegulations" />:</h3>
			<ul class="referencedRegulations">	
				<c:forEach items="${judgment.referencedRegulations}" var="referencedRegulation" >
					<li class="legalBase"> 
						<saos:lawJournalLink year="${referencedRegulation.lawJournalEntry.year}"
							 journalNo="${referencedRegulation.lawJournalEntry.journalNo}" entry="${referencedRegulation.lawJournalEntry.entry}" 
							 text="${referencedRegulation.rawText}" />
					</li>
				</c:forEach>
			</ul>
		</c:if>
	
		
		<c:if test="${!empty judgment.referencedCourtCases}">
		
			<div class="panel panel-default panel-generated" >
				<spring:message code="judgmentDetails.enrichmentTag.header" var="enrichmentTagHeader" />
				<spring:message code="judgmentDetails.enrichmentTag.hintText" var="enrichmentTagHintText" />
				<div class="panel-heading"><spring:message code="judgmentDetails.enrichmentTag.header" />:<saos:hint title="${enrichmentTagHeader}" content="${enrichmentTagHintText}" /></div>
				<div class="panel-body">
					<div class="label-title"><spring:message code="judgmentDetails.enrichmentTag.referencedCourtCases" />:</div>
					<div class="desc">
					
						<c:forEach items="${judgment.referencedCourtCases}" var="refCourtCase" >
							<p>
								<c:choose>
									<c:when test="${!empty refCourtCase.judgmentIds}" >
										<c:forEach items="${refCourtCase.judgmentIds}" var="judgmentId" >
											<a href="${contextPath}/judgments/${judgmentId}">
												<c:out value="${refCourtCase.caseNumber}"/>
											</a>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<c:url var="refCourtCaseUlrSearch" value="/search" >
											<c:param name="signature" value="${refCourtCase.caseNumber}" />
										</c:url>
										<a href="${refCourtCaseUlrSearch}">
											<c:out value="${refCourtCase.caseNumber}"/>
										</a>
									</c:otherwise>
								</c:choose>
							</p>
						</c:forEach>
					
					</div>
				</div>
			</div>
		
		</c:if>

	
		<div class="break" ></div>	
	
		<c:if test="${!empty judgment.sourceInfo}">
			<a href="${judgment.sourceInfo.sourceJudgmentUrl}" rel="nofollow" >
				<spring:message code="judgment.sourceLink" />
			</a>
		</c:if>
	
		
		
	</div>
	
</div>



<%-- Corrections --%>
<c:if test="${!empty corrections}">
	<div class="container correction-block">

		<div class="correction-info" >
			<spring:message code="judgmentDetails.corrections.info" /><a id="show-correction-box" href="" ><spring:message code="button.look" /></a>
		</div>
	
		<div class="corrections" id="corrections">
			
			<div class="correction-desc" >
				<spring:message code="judgmentDetails.corrections.sysInfo" />
			</div>
			
			<h3><spring:message code="judgmentDetails.corrections" />:</h3>
	
			<c:forEach items="${corrections}" var="correction">
				<saos:correction correction="${correction}"></saos:correction>
			</c:forEach>
		</div>
	</div>
</c:if>

<%-- Judgment summary --%>
<div class="container block" id="judgment-summary">
	<h2 ><spring:message code="judgmentDetails.judgmentFullText" /></h2>
	<div class="body">
		<c:out value="${judgment.textContent}" escapeXml="false" />
	</div>
</div>