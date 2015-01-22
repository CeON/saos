<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<c:forEach var="judgment" items="${judgments}" >

	<div class="judgment row" >
	
		<div class="row" >
		
				<c:if test="${!empty judgment.judgmentType && judgment.judgmentType != null}" >
					<div class="judgment-type" data-filter-value="${judgment.judgmentType}" >
						<saos:enum value="${judgment.judgmentType}" ></saos:enum>
					</div>
				</c:if>
				<div class="case-number">
					<a href="${contextPath}/judgments/${judgment.id}" title="<spring:message code="judgmentSearch.results.link"  />" >
						<saos:caseNumber items="${judgment.caseNumbers}"/>
					</a>
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
						<saos:propertyWithFilter value="${judgment.scCourtDivisionsChamberName}" filterValue="${judgment.scCourtDivisionId}" cssClass="chamber" />
						<span>-</span>
						<saos:propertyWithFilter value="${judgment.scCourtDivisionName}" filterValue="${judgment.scCourtDivisionsChamberId}" cssClass="chamber-division" />
					</div>
				
				</c:if>
				
				<c:if test="${!empty judgment.judges}" >
					<div class="judges" >
						<span>
							<spring:message code="judgment.judges" />:
						</span>
						<c:forEach items="${judgment.judges}" var="judge" >
							<div class="judge"><c:out value="${judge.name}" /></div>
						</c:forEach>	
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
						<c:forEach items="${judgment.keywords}" var="keyword" >
							<div class="keyword"><c:out value="${keyword}" /></div>
						</c:forEach>
					</div>
				</c:if>

				<div class="extract">${judgment.content}</div>
				
		</div>

	</div>

</c:forEach>

