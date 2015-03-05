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


	<%-- Judgment section navigation --%>
	<div class="info-box" >
		<div class="info-box-nav">
			<c:if test="${!empty corrections}">
				<a href="" id="corrections-nav" class="" data-placement="top" data-toggle="tooltip" title="<spring:message code="judgmentDetails.corrections.show" />" >
					<img src="${contextPath}/static/image/icons/corrections.png" alt="<spring:message code="judgmentDetails.corrections.iconAlt" />" height="28" />
				</a>
			</c:if>
		
			<c:if test="${!empty judgment.sourceInfo }">
				<a href="" id="source-info-nav" class="" data-placement="top" data-toggle="tooltip" title="<spring:message code="judgmentDetails.sourceInfo.show" />" >
					<img src="${contextPath}/static/image/icons/sourceInfo.png" alt="<spring:message code="judgmentDetails.sourceInfo.iconAlt" />" height="28" />
				</a>
			</c:if>
		</div>
	
	
		<%-- Judgment sections: corrections & sourceInfo--%>
	 	
		<%-- Corrections --%>
		<c:if test="${!empty corrections }">
		 	<div class="info-box-section" id="corrections-section" >
		 		<a id="corrections-hide" class="btn-hide" href="" data-placement="top" data-toggle="tooltip" title="<spring:message code="judgmentDetails.corrections.hide" />">
		 		</a>
		 	
		 		<div class="corrections" id="corrections">
					
					<h3><spring:message code="judgmentDetails.corrections.title" />:</h3>
			
					<c:forEach items="${corrections}" var="correction">
						<saos:correction correction="${correction}"></saos:correction>
					</c:forEach>
					
					<div class="correction-desc" >
						<spring:message code="judgmentDetails.corrections.sysInfo" />
					</div>
				</div>
		 	
		 	</div>
	 	</c:if>
	 	
	 	<%-- Judgment source info --%>
	 	<c:if test="${!empty judgment.sourceInfo}">
	 		<div class="info-box-section" id="source-info-section">
	 	
	 			<a id="source-info-hide" class="btn-hide" href="" data-placement="top" data-toggle="tooltip" title="<spring:message code="judgmentDetails.sourceInfo.hide" />" >
	 			</a>
	 			
				<div  class="info-box-content" >
					<h3><spring:message code="judgment.sourceInfo" />:</h3>
					<ul class="judgment-data judgment-source-info">
						<c:if test="${!empty judgment.sourceInfo.sourceJudgmentUrl}" >
							<li>
								<a href="${judgment.sourceInfo.sourceJudgmentUrl}" rel="nofollow" >
									<spring:message code="judgment.sourceLink" />
								</a>	
							</li>
						</c:if>
						<c:if test="${!empty judgment.sourceInfo.publicationDate}" >	
							<li>
								<div >
									<div class="label-title" ><spring:message code="judgmentDetails.sourceInfo.publicationDate" />:</div>
									<div class="desc" ><joda:format value="${judgment.sourceInfo.publicationDate}" pattern="${DATE_PATTERN}"/></div>
								</div>
							</li>
						</c:if>
						<c:if test="${!empty judgment.sourceInfo.publisher}" >
							<li>
								<div >
									<div class="label-title" ><spring:message code="judgmentDetails.sourceInfo.publisher" />:</div>
									<div class="desc" >${judgment.sourceInfo.publisher}</div>
								</div>
							</li>
						</c:if>
						<c:if test="${!empty judgment.sourceInfo.reviser}" >
							<li>
								<div >
									<div class="label-title" ><spring:message code="judgmentDetails.sourceInfo.reviser" />:</div>
									<div class="desc" >${judgment.sourceInfo.reviser}</div>
								</div>
							</li>
						</c:if>
						
					</ul>
				</div>
			
	 		</div>
	 	</c:if>
	
	</div>




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
	
	<div >
	
		<ul class="judgment-data">
		
			<c:if test="${!empty judgment.judgmentDate}" >
				<li>
					<div class="" >
						<div class="label-title" ><spring:message code="judgment.date" />:</div>
						<div class="desc" ><joda:format value="${judgment.judgmentDate}" pattern="${DATE_PATTERN}"/></div>
					</div>
				</li>
			</c:if>
		
		
			<%-- CourtType --%>	
			<c:if test="${!empty judgment.courtType }">
				<li>
					<div >
						<div class="label-title" ><spring:message code="judgment.courtType" />:</div>
						<div class="desc" >
							<saos:enum value="${judgment.courtType}" />
						</div>
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
								<span>
									<c:out value="${judge.name}" />
									<c:if test="${judge.presidingJudge}" >
										<span class="presiding-judge"  data-toggle="tooltip" title="<spring:message code="judgment.judgeRole.PRESIDING_JUDGE" />" >
											<img src="${contextPath}/static/image/icons/judge.png" alt="<spring:message code="judgment.judgeRole.PRESIDING_JUDGE.iconAlt" />" />
										</span>
									</c:if>
								</span>
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
			
			<c:if test="${!empty judgment.keywords}" >
				<li>
					<div class="" >
						<div class="label-title" ><spring:message code="judgment.keywords" />:</div>
						<div class="desc" >
							<c:forEach items="${judgment.keywords}" var="keyword" varStatus="status">
								<c:out value="${keyword.phrase}" /><c:if test="${!status.last}">,</c:if>
							</c:forEach>
						</div>
					</div>
				</li>
			</c:if>
			
			<c:if test="${!empty judgment.receiptDate}" >
				<li>
					<div class="" >
						<div class="label-title" ><spring:message code="judgment.receiptDate" />:</div>
						<div class="desc" ><joda:format value="${judgment.receiptDate}" pattern="${DATE_PATTERN}"/></div>
					</div>
				</li>
			</c:if>
			
			<c:if test="${!empty judgment.meansOfAppeal }">
				<li>
					<div >
						<div class="label-title" ><spring:message code="judgment.meansOfAppeal" />:</div>
						<div class="desc" >
							<c:out value="${judgment.meansOfAppeal.name}" />
						</div>
					</div>
				</li>
			</c:if>
			
			<c:if test="${!empty judgment.judgmentResult }">
				<li>
					<div >
						<div class="label-title" ><spring:message code="judgment.judgmentResult" />:</div>
						<div class="desc" >
							<c:out value="${judgment.judgmentResult.text}" />
						</div>
					</div>
				</li>
			</c:if>
			
			<c:if test="${!empty judgment.lowerCourtJudgments}" >
				<li>
					<div class="" >
						<div class="label-title" ><spring:message code="judgment.lowerCourtJudgments" />:</div>
						<div class="desc" >
							<c:forEach items="${judgment.lowerCourtJudgments}" var="lowerCourt" varStatus="status">
								<c:out value="${lowerCourt}" /><c:if test="${!status.last}">,</c:if>
							</c:forEach>
						</div>
					</div>
				</li>
			</c:if>
			
			
		</ul>
	
	
				
			
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

						<p>					
							<c:forEach items="${judgment.referencedCourtCases}" var="refCourtCase" >

								<c:choose>
									<c:when test="${!empty refCourtCase.judgmentIds}" >
										<c:forEach items="${refCourtCase.judgmentIds}" var="judgmentId" >
											<a href="${contextPath}/judgments/${judgmentId}">
												<c:out value="${refCourtCase.caseNumber}"/>
											</a>
										</c:forEach>
									</c:when>
									<c:otherwise>
										 <spring:url value="/search" var="refCourtCaseUlrSearch" htmlEscape="true" >
										 	<spring:param name="signature" value="${refCourtCase.caseNumber}" />
										 </spring:url>
										 
										<a href="${refCourtCaseUlrSearch}" >
											<c:out value="${refCourtCase.caseNumber}"/>
										</a>
									</c:otherwise>
								</c:choose>
							
							</c:forEach>
						</p>
					
					</div>
				</div>
			</div>
		</c:if>

	</div>
	
</div>


<%-- Judgment content --%>
<div class="container block" id="judgment-content">
	<h2 ><spring:message code="judgmentDetails.judgmentFullText" /></h2>
	<div class="body">
		<c:out value="${judgment.textContent}" escapeXml="false" />
	</div>
</div>
