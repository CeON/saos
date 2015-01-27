<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<script>
$(document).ready(function() {
	jsInitInJudgmentDetails();
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
									
									<c:if test="${!empty judge.specialRoles}" >
										(
										<c:forEach items="${judge.specialRoles}" var="role"  >
											<saos:enum value="${role}" />
										</c:forEach>
										)
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
			<h4><spring:message code="judgment.keywords" />:</h4>
			<div class="keywords">	
				<c:forEach items="${judgment.keywords}" var="keyword" >
					<div class="keyword"><c:out value="${keyword.phrase}" /></div>
				</c:forEach>
			</div>
		</c:if>
		
			
		<c:if test="${!empty judgment.legalBases}" >
			<h4><spring:message code="judgment.legalBases" />:</h4>
			<div class="legalBases">	
				<c:forEach items="${judgment.legalBases}" var="legalBase" >
					<div class="legalBase"><c:out value="${legalBase}" /></div>
				</c:forEach>
			</div>
		</c:if>
		
		<c:if test="${!empty judgment.referencedRegulations}" >
			<h4><spring:message code="judgment.referencedRegulations" />:</h4>
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
	

	
		<div class="break" ></div>
	
		 <!-- Button trigger modal -->
		<button class="btn btn-primary" data-toggle="modal" data-target="#myModal">
		  <spring:message code="judgmentDetails.button.fullText" />
		</button>
	
		<!-- Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only"><spring:message code="button.close" /></span></button>
		        <h2 class="modal-title" id="myModalLabel"><spring:message code="judgmentDetails.judgmentFullText" /> </h2>
		      </div>
		      <div class="modal-body">
		      	<c:out value="${judgment.textContent}" escapeXml="false" />
		      </div>

		    </div>
		  </div>
		</div><!-- Modal end -->
		
	</div>
	
</div>



<%-- Corrections --%>
<c:if test="${!empty corrections}">
	<div class="container correction-block">

		<div class="correction-info" >
			<spring:message code="judgmentDetails.corrections.info" /><span id="show-correction-box"><spring:message code="button.look" /></span>
		</div>
	
		<div class="corrections" id="corrections">
			
			<div class="correction-desc" >
				<spring:message code="judgmentDetails.corrections.sysInfo" />
			</div>
			
			<h4><spring:message code="judgmentDetails.corrections" />:</h4>
	
			<c:forEach items="${corrections}" var="correction">
				<saos:correction correction="${correction}"></saos:correction>
			</c:forEach>
		</div>
	</div>
</c:if>
