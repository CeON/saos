<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<ol>
<c:forEach var="judgment" items="${judgments}" >

	<li class="judgment row" >


		<%-- Top line --%>
		<div class="top-line">
			<div class="top-line-main">
				<c:if test="${!empty judgment.judgmentType && judgment.judgmentType != null}" >
				
					<a href="" class="judgment-type" data-filter-value="${judgment.judgmentType}" >
						<saos:enum value="${judgment.judgmentType}" ></saos:enum>
					</a>

				</c:if>
			
				<c:if test="${!empty judgment.courtType && judgment.courtType != null}" >
					<span>-</span>
					<a href="" class="court-type" data-filter-value="${judgment.courtType}" ><spring:message code="judgmentSearch.results.courtType.${judgment.courtType}" /></a>
				</c:if>
			
			</div>
			
			<span class="top-line-right">
				<spring:message code="judgmentSearch.results.referencingCount" />: <c:out value="${judgment.referencingCount}" />
			</span>
		</div>
	
		<div class="case-number">
			<h3>
				<a href="${contextPath}/judgments/${judgment.id}" title="<spring:message code="judgmentSearch.results.link"  />" >
					<saos:caseNumber items="${judgment.caseNumbers}"/>
				</a>
			</h3>
		</div>
		
		<div class=""><span><spring:message code="judgment.date" />:</span><a href="" class="date"><joda:format value="${judgment.judgmentDate}" pattern="${DATE_PATTERN}" /></a></div>
		
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
							<a href="" class="judge"><c:out value="${judge.name}" /></a>
							
							<c:if test="${judge.presidingJudge}" >
								<span class="presiding-judge"  data-toggle="tooltip" title="<spring:message code="judgment.judgeRole.PRESIDING_JUDGE" />" >
									<img src="${contextPath}/static/image/icons/judge.png" alt="<spring:message code="judgment.judgeRole.PRESIDING_JUDGE.iconAlt" />" height="16" />
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
				<a href="" class="judgment-form">
					<c:out value="${judgment.scJudgmentForm}" />
				</a>	
			</div>
		</c:if>
		
		<c:if test="${!empty judgment.keywords}" >
			<div class="keywords">	
				<span>
					<spring:message code="judgment.keywords" />:
				</span>
				<ul>
					<c:forEach items="${judgment.keywords}" var="keyword" >
						<li ><a href="" class="keyword"><c:out value="${keyword}" /></a></li>
					</c:forEach>
				</ul>
			</div>
		</c:if>

		<p class="extract">${judgment.content}</p>
	
	</li>

</c:forEach>
</ol>
