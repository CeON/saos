<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>




<a href="${contextPath}/" class="saos-logo" aria-label="<spring:message code='linkToMainPage'/>"></a>


<div class="judgment-details" id="judgment" tabindex=0>

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
                                <c:url value="/search" var="judgeSearchUrl">
                                    <c:param name="judgeName" value="&quot;${fn:replace(judge.name, '\"', ' ')}&quot;" />
                                </c:url>
                                <a href="${judgeSearchUrl}" data-toggle="tooltip" title="${judgeLinkTooltip}" >
                                    <c:out value="${judge.name}" />
                                </a>
	                            <span class="presiding-judge-white"  data-toggle="tooltip" title="<spring:message code="judgment.judgeRole.PRESIDING_JUDGE" />" >
	                            </span>
                            </span>
                        </c:forEach>
                        
                        <%-- Not Presiding Judges --%>
                        <c:forEach items="${judgment.judges}" var="judge" >
                            <c:if test="${!judge.presidingJudge}" >
                                <span>                                     
                                    <c:url value="/search" var="judgeSearchUrl">
                                        <c:param name="judgeName" value="&quot;${fn:replace(judge.name, '\"', ' ')}&quot;" />
                                    </c:url>
                                    <a href="${judgeSearchUrl}" data-toggle="tooltip" title="${judgeLinkTooltip}" >
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
                                <c:out value="${reporter}" />
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
                        <c:url value="/search" var="legalBaseSearchUrl">
                            <c:param name="legalBase" value="&quot;${fn:replace(judge.name, '\"', ' ')}&quot;" />
                        </c:url>
                        <a href="${legalBaseSearchUrl}" data-toggle="tooltip" title="${legalBasesLinkTooltip}" >
                           <c:out value="${legalBase}" />
                        </a>
                    </div>
                </c:forEach>
            </div>
        </c:if>
        
        <%-- Referenced Regulations --%>
        <c:if test="${!empty judgment.referencedRegulations}" >
            <spring:message code="judgmentDetails.linkTooltip.referencedRegulations" var="referencedRegulationLinkTooltip" />
            
            <h3><spring:message code="judgment.referencedRegulations" />:</h3>
            <ol class="referencedRegulations">	
                <c:forEach items="${judgment.referencedRegulations}" var="referencedRegulation" >
                    
                    <c:set var="generatedClass">${(referencedRegulation.generated) ? "generated" : ""}</c:set>
                    <li class="referencedRegulation ${generatedClass}">
                        
                        <saos:lawJournalLink lawJournalEntry="${referencedRegulation.lawJournalEntry}" text="${referencedRegulation.rawText}"
                            tooltipText="${referencedRegulationLinkTooltip}" />
                        
                        
                        <c:if test="${referencedRegulation.generated}">
                            <spring:message code="judgmentDetails.enrichmentTag.referencedRegulation.hint.title" var="referencedRegulationGeneratedHintTitle" />
                            <spring:message code="judgmentDetails.enrichmentTag.referencedRegulation.hint.text" var="referencedRegulationGeneratedHintText" />
                            
                            <saos:hint title="${referencedRegulationGeneratedHintTitle}" content="${referencedRegulationGeneratedHintText}" placement="left" />
                            
                        </c:if>
                        
                    </li>
                    
                </c:forEach>
            </ol>
        </c:if>
        
        
        <%-- Corrections --%>
        <c:if test="${!empty corrections }">
            <div class="info-box-section">
             
                
                <a id="corrections-toggle" role="button" aria-expanded="false" class="info-section-button" href="" data-placement="top" data-toggle="tooltip" title="<spring:message code="judgmentDetails.corrections.toggle" />">
                    <h4><spring:message code="judgmentDetails.corrections.title" />:</h4>
                    <span></span>
                </a>
             
                
                
                <div id="corrections-section" >
                
                    <c:forEach items="${corrections}" var="correction">
                        <saos:correction correction="${correction}"></saos:correction>
                    </c:forEach>
                    
                    <div class="correction-desc" >
                        <small><i><spring:message code="judgmentDetails.corrections.sysInfo" /></i></small>
                    </div>
                
                </div>
             
             </div>
        </c:if>
        
        <%-- Judgment source info --%>
        <c:if test="${!empty judgment.sourceInfo}">
            <div class="info-box-section" >
        
                <a id="source-info-toggle" role="button" aria-expanded="false" class="info-section-button" href="" data-placement="top" data-toggle="tooltip" title="<spring:message code="judgmentDetails.sourceInfo.toggle" />" >
                
                    <h4><spring:message code="judgment.sourceInfo" />:</h4>
                    <span></span>
                </a>
                
                
                
                <div id="source-info-section" >
                    
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
    
        <c:set var="showGeneratedPanel" value="${(!empty judgment.referencedCourtCases) || referencingCount > 0 }" />
        <c:if test="${showGeneratedPanel}">
            <spring:message code="judgmentDetails.enrichmentTag.header" var="enrichmentTagHeader" />
            <spring:message code="judgmentDetails.enrichmentTag.hintText" var="enrichmentTagHintText" />
        
            <h4><spring:message code="judgmentDetails.enrichmentTag.header" />:<saos:hint title="${enrichmentTagHeader}" content="${enrichmentTagHintText}" placement="left" /></h4>
            
            <ul>
            <c:if test="${!empty judgment.referencedCourtCases}">
                <li>
                    <div class="" >
                        <div class="label-title" ><spring:message code="judgmentDetails.enrichmentTag.referencedCourtCases" />:</div>
                        <div class="desc" >
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
                        </div>
                    </div>
                
                </li>
            
            </c:if>
            <c:if test="${referencingCount > 0}">
                <li>
                    <div class="" >
                        <div class="label-title" ><spring:message code="judgmentDetails.enrichmentTag.referencedCourtCases" />:</div>
                        <div class="desc" >
	                        <a href="${contextPath}/search?referencedCourtCaseId=${judgment.id}" title="<spring:message code="judgmentDetails.enrichmentTag.referencingJudgments.searchLink" />">
	                            <spring:message code="judgmentDetails.enrichmentTag.referencingJudgments.count" />: <c:out value="${referencingCount}" />
	                        </a> 
                        </div>
                    </div>
                </li>
            </c:if>
        
            </ul>
        </c:if>

    </div>


</div>