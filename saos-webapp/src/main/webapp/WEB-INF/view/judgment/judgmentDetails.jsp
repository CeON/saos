<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<c:set var="isCCJudgment" value="${judgment.instanceOfCommonCourtJudgment}" />

<div class="container judgment-page block">

	<h4><span><spring:message code="judgment.${fn:toLowerCase(judgment.judgmentType)}" /></h2>
	<h2>		
		<saos:caseNumber items="${judgment.caseNumbers}"/>
	</h2>
	
	<div class="col-md-12" >
	
		<ul class="judgment-data">
			<li>
				<div class="" >
					<div class="label-title" ><spring:message code="judgment.date" />:</div>
					<div class="desc" ><joda:format value="${judgment.judgmentDate}" pattern="${DATE_PATTERN}"/></div>
				</div>
			</li>
			<li>
				<div class="" >
					<div class="label-title" ><spring:message code="judgment.court" />:</div>
					<div class="desc" >
						<c:if test="${isCCJudgment && !empty judgment.courtDivision}" >
							<c:out value="${judgment.courtDivision.court.name}" />
						</c:if>
					</div>
				</div>
			</li>
			<li>
				<div class="" >
					<div class="label-title" ><spring:message code="judgment.division" />:</div>
					<div class="desc" >
						<c:if test="${isCCJudgment && !empty judgment.courtDivision}" >
							<c:out value="${judgment.courtDivision.name}" />
						</c:if>
					</div>
				</div>
			</li>
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
										<spring:message code="judgment.judge.${fn:toLowerCase(role)}" />
									</c:forEach>
									)
								</c:if>
							</p>
						</c:forEach>
					</div>
				</div>
			</li>
			
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
			
		</ul>
	
		<c:if test="${isCCJudgment && !empty judgment.keywords}" >
			<h4><spring:message code="judgment.keywords" />:</h4>
			<div class="keywords">	
				<c:forEach items="${judgment.keywords}" var="keyword" >
					<div class="keyword"><c:out value="${keyword.phrase}" /></div>
				</c:forEach>
			</div>
		</c:if>
		
		<h4><spring:message code="judgment.legalbases" />:</h4>
		<div class="legalBases">	
			<c:forEach items="${judgment.legalBases}" var="legalBase" >
				<div class="legalBase"><c:out value="${legalBase}" /></div>
			</c:forEach>
		</div>
		
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
	
		</br>
		</br>
		</br>
		</br>
	
		 <!-- Button trigger modal -->
		<button class="btn btn-primary" data-toggle="modal" data-target="#myModal">
		  <spring:message code="judgment.button.fullText" />
		</button>
	
		<!-- Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		        <h4 class="modal-title" id="myModalLabel"><spring:message code="judgment.full.header" /> </h4>
		      </div>
		      <div class="modal-body">
		      	<c:out value="${judgment.textContent}" escapeXml="false" />
		      </div>

		    </div>
		  </div>
		</div><!-- Modal end -->
		
	</div>
	
</div>

