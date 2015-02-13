<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<ol>
<c:forEach var="judgment" items="${judgments}" >

	<li class="judgment row" >
	
		<div class="case-number">
			<h3>
				<a href="${contextPath}/judgments/${judgment.id}" title="<spring:message code="judgmentSearch.results.link"  />" >
					<saos:caseNumber items="${judgment.caseNumbers}"/>
				</a>
			</h3>
		</div>
		
		<%-- Top line --%>
		<div class="top-line" >
			<c:if test="${!empty judgment.judgmentType && judgment.judgmentType != null}" >
				
				<div class="judgment-type" data-filter-value="${judgment.judgmentType}" >
					<saos:enum value="${judgment.judgmentType}" ></saos:enum>
				</div>

			</c:if>
			
			<c:if test="${!empty judgment.courtType && judgment.courtType != null}" >
				<span>-</span>
				<div class="court-type" data-filter-value="${judgment.courtType}" ><spring:message code="judgmentSearch.results.courtType.${judgment.courtType}" /></div>
			</c:if>
		</div>
		
		
		<div class=""><span><spring:message code="judgment.date" />:</span><span class="date"><joda:format value="${judgment.judgmentDate}" pattern="${DATE_PATTERN}" /></span></div>
		
		<%-- CommonCourt & CommonCourtDivision  --%>
		<c:if test="${!empty judgment.ccCourtName || !empty judgment.ccCourtDivisionName}" >
		
			<div class="court-desc">					
				<saos:propertyWithFilter value="${judgment.ccCourtName}" filterValue="${judgment.ccCourtId}" cssClass="court" />
				<span>-</span>
				<saos:propertyWithFilter value="${judgment.ccCourtDivisionName}" filterValue="${judgment.ccCourtDivisionId}" cssClass="division" /> 
			</div>
		
		</c:if>
		
		<%-- SupremeCourtChamber & SupremeCourtChamberDivision  --%>
		<c:if test="${!empty judgment.scCourtDivisionsChamberName || !empty judgment.scCourtDivisionName}" >
		
			<div class="court-desc">
				<saos:propertyWithFilter value="${judgment.scCourtDivisionsChamberName}" filterValue="${judgment.scCourtDivisionsChamberId}" cssClass="chamber" />
				<span>-</span>
				<saos:propertyWithFilter value="${judgment.scCourtDivisionName}" filterValue="${judgment.scCourtDivisionId}" cssClass="chamber-division" />
			</div>
		
		</c:if>
		
		<c:if test="${!empty judgment.judges}" >
			<div class="judges" >
				<span>
					<spring:message code="judgment.judges" />:
				</span>
				<ul>
					<c:forEach items="${judgment.judges}" var="judge" >
				
						<li >
							<span class="judge"><c:out value="${judge.name}" /></span>
							
							<c:if test="${judge.presidingJudge}" >
								<span class="presiding-judge"  data-toggle="tooltip" title="<spring:message code="judgment.judgeRole.PRESIDING_JUDGE" />" >
									<img src="${contextPath}/static/image/icons/judge.png" alt="<spring:message code="judgment.judgeRole.PRESIDING_JUDGE.iconAlt" />" />
								</span>
							</c:if>
						</li>
					</c:forEach>
				</ul>	
			</div>
		</c:if>
		
		<%-- SupremeCourtJudgmentForm --%>
		<c:if test="${!empty judgment.scJudgmentForm}" >
			<div class="" >
				<span>
					<spring:message code="judgment.scJudgmentForm" />:
				</span>
				<span class="judgment-form">
					<c:out value="${judgment.scJudgmentForm}" />
				</span>	
			</div>
		</c:if>
		
		<c:if test="${!empty judgment.keywords}" >
			<div class="keywords">	
				<span>
					<spring:message code="judgment.keywords" />:
				</span>
				<ul>
					<c:forEach items="${judgment.keywords}" var="keyword" >
						<li class="keyword"><c:out value="${keyword}" /></li>
					</c:forEach>
				</ul>
			</div>
		</c:if>

		<p class="extract">${judgment.content}</p>
	
	</li>

</c:forEach>
</ol>
