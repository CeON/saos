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
	    <spring:message code="judgmentDetails.linkTooltip.judgmentType" var="judgmentTypeLinkTooltip" />
		<div class="judgment-type">
		    <a href="${contextPath}/search?judgmentTypes=${judgment.judgmentType}" data-toggle="tooltip" title="${judgmentTypeLinkTooltip}" >
			    <spring:message code="judgment.${fn:toLowerCase(judgment.judgmentType)}" />
			</a>
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
			    <spring:message code="judgmentDetails.linkTooltip.date" var="dateLinkTooltip" />
			    <c:set var="judgmentDate">
			        <joda:format value="${judgment.judgmentDate}" pattern="${DATE_PATTERN}"/>
			    </c:set>
				<li>
					<div class="" >
						<div class="label-title" ><spring:message code="judgment.date" />:</div>
						<div class="desc" >
							<a href="${contextPath}/search?dateFrom=${judgmentDate}&amp;dateTo=${judgmentDate}" data-toggle="tooltip" title="${dateLinkTooltip}" >
							  ${judgmentDate}
							</a>
						</div>
					</div>
				</li>
			</c:if>
		
		
			<%-- CourtType --%>	
			<c:if test="${!empty judgment.courtType }">
			    <spring:message code="judgmentDetails.linkTooltip.courtType" var="courtTypeLinkTooltip" />
				<li>
					<div >
						<div class="label-title" ><spring:message code="judgment.courtType" />:</div>
						<div class="desc" >
						    <a href="${contextPath}/search?courtCriteria.courtType=${judgment.courtType}" data-toggle="tooltip" title="${courtTypeLinkTooltip}" >
							    <saos:enum value="${judgment.courtType}" />
							</a>
						</div>
					</div>
				</li>
			</c:if>
			
			
			<%-- CommonCourtJudgment properties --%>
			<c:if test="${judgment.courtType == 'COMMON'}" >
			
				<c:if test="${!empty judgment.courtDivision.court.name}" >
				    <spring:message code="judgmentDetails.linkTooltip.commonCourt" var="commonCourtLinkTooltip" />
					<li>
						<div class="" >
							<div class="label-title" ><spring:message code="judgment.commonCourt" />:</div>
							<div class="desc" >
								<a href="${contextPath}/search?courtCriteria.courtType=${judgment.courtType}&amp;courtCriteria.ccCourtId=${judgment.courtDivision.court.id}" data-toggle="tooltip" title="${commonCourtLinkTooltip}" >
									<c:out value="${judgment.courtDivision.court.name}" />
								</a>
							</div>
						</div>
					</li>
				</c:if>
				
				<c:if test="${!empty judgment.courtDivision.name}" >
				    <spring:message code="judgmentDetails.linkTooltip.commonCourtDivision" var="commonCourtDivisionLinkTooltip" />
					<li>
						<div class="" >
							<div class="label-title" ><spring:message code="judgment.commonDivision" />:</div>
							<div class="desc" >
								<a href="${contextPath}/search?courtCriteria.courtType=${judgment.courtType}&amp;courtCriteria.ccCourtId=${judgment.courtDivision.court.id}&amp;courtCriteria.ccCourtDivisionId=${judgment.courtDivision.id}" data-toggle="tooltip" title="${commonCourtDivisionLinkTooltip}" >
									<c:out value="${judgment.courtDivision.name}" />
								</a>
							</div>
						</div>
					</li>
				</c:if>
			
			</c:if>
			
			
			<%-- SupremeCourtJudgment properties --%>
			<c:if test="${judgment.courtType == 'SUPREME'}" >
			
				<c:if test="${!empty judgment.scChambers}" >
				    <spring:message code="judgmentDetails.linkTooltip.scCourtChamber" var="scCourtChamberLinkTooltip" />
					<li>
						<div class="" >
							<div class="label-title" ><spring:message code="judgment.supremeChambers" />:</div>
							<div class="desc" >
								<c:forEach items="${judgment.scChambers}" var="chamber">
								    <a href="${contextPath}/search?courtCriteria.courtType=${judgment.courtType}&amp;courtCriteria.scCourtChamberId=${chamber.id}" data-toggle="tooltip" title="${scCourtChamberLinkTooltip}" >
									   <c:out value="${chamber.name}" />
									</a>
								</c:forEach>	
							</div>
						</div>
					</li>
				</c:if>
				
				<c:if test="${!empty judgment.scChamberDivision.name}" >
				    <spring:message code="judgmentDetails.linkTooltip.scCourtChamberDivision" var="scCourtChamberDivisionLinkTooltip" />
					<li>
						<div class="" >
							<div class="label-title" ><spring:message code="judgment.supremeChamberDivisionFullName" />:</div>
							<div class="desc" >
							    <a href="${contextPath}/search?courtCriteria.courtType=${judgment.courtType}&amp;courtCriteria.scCourtChamberId=${judgment.scChamberDivision.scChamber.id}&amp;courtCriteria.scCourtChamberDivisionId=${judgment.scChamberDivision.id}" data-toggle="tooltip" title="${scCourtChamberDivisionLinkTooltip}" >
									<c:out value="${judgment.scChamberDivision.scChamber.name}" /> 
									<c:out value="${judgment.scChamberDivision.name}" />
								</a>
							</div>
						</div>
					</li>
				</c:if>
				
				<c:if test="${!empty judgment.scJudgmentForm}" >
				    <spring:message code="judgmentDetails.linkTooltip.scJudgmentForm" var="scJudgmentFormLinkTooltip" />
					<li>
						<div class="" >
							<div class="label-title" ><spring:message code="judgment.scJudgmentForm" />:</div>
							<div class="desc" >
							    <a href="${contextPath}/search?courtCriteria.courtType=${judgment.courtType}&amp;scJudgmentForm=${judgment.scJudgmentForm.name}" data-toggle="tooltip" title="${scJudgmentFormLinkTooltip}" >
								    <c:out value="${judgment.scJudgmentForm.name}" />
								</a>
							</div>
						</div>
					</li>
				</c:if>
				
				<c:if test="${!empty judgment.personnelType}" >
				    <spring:message code="judgmentDetails.linkTooltip.scPersonnelType" var="scPersonnelTypeLinkTooltip" />
					<li>
						<div class="" >
							<div class="label-title" ><spring:message code="judgment.personnelType" />:</div>
							<div class="desc" >
							    <a href="${contextPath}/search?courtCriteria.courtType=${judgment.courtType}&amp;scPersonnelType=${judgment.personnelType}" data-toggle="tooltip" title="${scPersonnelTypeLinkTooltip}" >
								    <saos:enum value="${judgment.personnelType}" />
								</a>
							</div>
						</div>
					</li>
				</c:if>
			
			</c:if>
			
			<c:if test="${!empty judgment.judges}" >
            <spring:message code="judgmentDetails.linkTooltip.judge" var="judgeLinkTooltip" />
			<li>
				<div class="" >
					<div class="label-title" ><spring:message code="judgment.judges" />:</div>
					<div class="desc" >
					
						<%-- Presiding Judges --%>
						<spring:eval expression="judgment.getJudges(T(pl.edu.icm.saos.persistence.model.Judge.JudgeRole).PRESIDING_JUDGE)" var="presidingJudges" />
						
						<c:forEach items="${presidingJudges}" var="judge" >
							<span>
							<a href="${contextPath}/search?judgeName=${judge.name}" data-toggle="tooltip" title="${judgeLinkTooltip}" >
                                       <c:out value="${judge.name}" />
                                    </a>
						    <span class="presiding-judge"  data-toggle="tooltip" title="<spring:message code="judgment.judgeRole.PRESIDING_JUDGE" />" >
							     <img src="${contextPath}/static/image/icons/judge.png" alt="<spring:message code="judgment.judgeRole.PRESIDING_JUDGE.iconAlt" />" />
							</span>
							</span>
						</c:forEach>
						
						<%-- Not Presiding Judges --%>
						<c:forEach items="${judgment.judges}" var="judge" >
							<c:if test="${!judge.presidingJudge}" >
								<span>									    
								    <a href="${contextPath}/search?judgeName=${judge.name}" data-toggle="tooltip" title="${judgeLinkTooltip}" >
                                       <c:out value="${judge.name}" />
                                    </a>
								</span>
							</c:if>
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
			    <spring:message code="judgmentDetails.linkTooltip.keywords" var="keywordsLinkTooltip" />
				<li>
					<div class="" >
						<div class="label-title" ><spring:message code="judgment.keywords" />:</div>
						<div class="desc" >
							<c:forEach items="${judgment.keywords}" var="keyword" varStatus="status">
							<a href="${contextPath}/search?courtCriteria.courtType=${judgment.courtType}&amp;keywords=${keyword.phrase}" data-toggle="tooltip" title="${keywordsLinkTooltip}" >
								<c:out value="${keyword.phrase}" />
							</a>
							<c:if test="${!status.last}">,</c:if>
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
		    <spring:message code="judgmentDetails.linkTooltip.legalBases" var="legalBasesLinkTooltip" />
			<h3><spring:message code="judgment.legalBases" />:</h3>
			<div class="legalBases">	
				<c:forEach items="${judgment.legalBases}" var="legalBase" >
					<div class="legalBase">
						<a href="${contextPath}/search?legalBase=${legalBase}" data-toggle="tooltip" title="${legalBasesLinkTooltip}" >
						   <c:out value="${legalBase}" />
						</a>
					</div>
				</c:forEach>
			</div>
		</c:if>
		
		<c:if test="${!empty judgment.referencedRegulations}" >
			<spring:message code="judgmentDetails.linkTooltip.referencedRegulations" var="referencedRegulationLinkTooltip" />
			
			<h3><spring:message code="judgment.referencedRegulations" />:</h3>
			<ul class="referencedRegulations">	
				<c:forEach items="${judgment.referencedRegulations}" var="referencedRegulation" >
					
					<li class="referencedRegulation">
						
						<c:choose>
							<c:when test="${referencedRegulation.generated}">
							
								<div class="generated">
									<saos:lawJournalLink lawJournalEntry="${referencedRegulation.lawJournalEntry}" text="${referencedRegulation.rawText}"
										tooltipText="${referencedRegulationLinkTooltip}" />
								
									<div class="generated-hint">	 
										<spring:message code="judgmentDetails.enrichmentTag.referencedRegulation.hint.title" var="referencedRegulationGeneratedHintTitle" />
										<spring:message code="judgmentDetails.enrichmentTag.referencedRegulation.hint.text" var="referencedRegulationGeneratedHintText" />
									
										<saos:hint title="${referencedRegulationGeneratedHintTitle}" content="${referencedRegulationGeneratedHintText}" />
									</div>
								</div>
					
							</c:when>
							<c:otherwise>
							
								<div>
									<saos:lawJournalLink lawJournalEntry="${referencedRegulation.lawJournalEntry}" text="${referencedRegulation.rawText}"
										tooltipText="${referencedRegulationLinkTooltip}" />
								</div>
						
							</c:otherwise>
						</c:choose>
						
					</li>
					
						
				</c:forEach>
			</ul>
		</c:if>
	
		<c:set var="showGeneratedPanel" value="${(!empty judgment.referencedCourtCases) || referencingCount > 0 }" />
		
		<c:if test="${showGeneratedPanel}">
		
			<div class="panel panel-default panel-generated" >
				<spring:message code="judgmentDetails.enrichmentTag.header" var="enrichmentTagHeader" />
				<spring:message code="judgmentDetails.enrichmentTag.hintText" var="enrichmentTagHintText" />
				<div class="panel-heading"><spring:message code="judgmentDetails.enrichmentTag.header" />:<saos:hint title="${enrichmentTagHeader}" content="${enrichmentTagHintText}" /></div>
				
				<c:if test="${!empty judgment.referencedCourtCases}">
				<div class="panel-body">
					<div class="col-xs-12 label-title"><spring:message code="judgmentDetails.enrichmentTag.referencedCourtCases" />:</div>
					<div class="col-xs-12 desc">

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
										<span> 
										  <c:out value="${refCourtCase.caseNumber}"/>
										</span>
									</c:otherwise>
								</c:choose>
							
							</c:forEach>
						</p>
					
					</div>
				</div>
				</c:if>
				<c:if test="${referencingCount > 0}">
				<div class="panel-body">
					<div class="label-title"><spring:message code="judgmentDetails.enrichmentTag.referencingJudgments" />:</div>
					<div class="desc">
						<a href="${contextPath}/search?referencedCourtCaseId=${judgment.id}" title="<spring:message code="judgmentDetails.enrichmentTag.referencingJudgments.searchLink" />">
							<spring:message code="judgmentDetails.enrichmentTag.referencingJudgments.count" />: <c:out value="${referencingCount}" />
						</a> 
					</div>
				</div>
				</c:if>
			</div>
		</c:if>

	</div>
	
</div>


<%-- Judgment content --%>

<div class="container judgment-page-content block" id="judgment-content">
			
	<h2 ><spring:message code="judgmentDetails.judgmentFullText" /></h2>
	
	<div class="info-box" >
		<div class="info-box-nav">
			<spring:message code="judgmentDetails.judgmentFullText.hint.title" var="fullTextHintTitle" />
			<spring:message code="judgmentDetails.judgmentFullText.hint" var="fullTextHint" />
			<saos:hint title="${fullTextHintTitle}" content="${fullTextHint}" placement="left" />
		</div>
	</div>
			
	<c:choose>
		<c:when test="${judgment.textContent.contentInFile}">
			<c:set var="filePath" value="${contextPath}/files/judgments/${judgment.textContent.filePath}" />
		</c:when>
		<c:otherwise>
			<c:set var="filePath" value="${contextPath}/judgments/content/${judgmentId}.html" />
		</c:otherwise>
	</c:choose>
	
	<c:set var="filetypeIconPath" value="${contextPath}/static/image/icons/filetype/${fn:toLowerCase(judgment.textContent.type)}.png" />
	<c:set var="filetypeIconAlt"><saos:enum value="${judgment.textContent.type}" /></c:set>
	<c:set var="downloadFileTextMessage"><spring:message code="judgmentDetails.judgmentFullText.download.${fn:toLowerCase(judgment.textContent.type)}" /></c:set>
				
	<div class="judgment-content-buttons">
		<a class="" href="${filePath}">
			<img src="${filetypeIconPath}" alt="${filetypeIconAlt}" height="48" />
			<c:out value="${downloadFileTextMessage}" />
		</a>
	</div>
	
	<div class="body">
		<c:out value="${formattedTextContent}" escapeXml="false" />
	</div>
	
</div>
