<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<div class="container judgment-page block">

	<c:if test="${!empty judgment.judgmentType}" >
		<div class="type">
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
		
			<c:if test="${!empty judgment.scChambers}" >
				<li>
					<div class="" >
						<div class="label-title" ><spring:message code="judgment.chamber" />:</div>
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
						<div class="label-title" ><spring:message code="judgment.chamberdivision" />:</div>
						<div class="desc" >
							<c:out value="${judgment.scChamberDivision.name}" />
						</div>
					</div>
				</li>
			</c:if>
			
			<c:if test="${!empty judgment.personnelType}" >
				<li>
					<div class="" >
						<div class="label-title" ><spring:message code="judgment.personneltype" />:</div>
						<div class="desc" >
							<c:set var="personnelType" value="judgment.personneltype.${fn:toLowerCase(judgment.personnelType)}" />
							<spring:message code="${personnelType}" />
						</div>
					</div>
				</li>
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
											<spring:message code="judgment.judge.${fn:toLowerCase(role)}" />
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
	
	
		<%--
		<c:if test="${!empty judgment.keywords}" >
			<h4><spring:message code="judgment.keywords" />:</h4>
			<div class="keywords">	
				<c:forEach items="${judgment.keywords}" var="keyword" >
					<div class="keyword"><c:out value="${keyword.phrase}" /></div>
				</c:forEach>
			</div>
		</c:if>
		
		 --%>
		
		<c:if test="${!empty judgment.legalBases}" >
			<h4><spring:message code="judgment.legalbases" />:</h4>
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

