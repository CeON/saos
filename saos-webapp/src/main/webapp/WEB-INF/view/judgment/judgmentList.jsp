<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<c:forEach var="judgment" items="${judgments}" >

	<div class="judgment row" >
	
		<div class="row" >
		
				<c:if test="${!empty judgment.judgmentType && judgment.judgmentType != null}" >
					<div class="type" data-judgment-type="${judgment.judgmentType}" >
						<saos:enum value="${judgment.judgmentType}" ></saos:enum>
					</div>
				</c:if>
				<div class="case-number">
					<a href="${contextPath}/judgments/${judgment.id}">
						<saos:caseNumber items="${judgment.caseNumbers}"/>
					</a>
				</div>
				
				<div class=""><span><spring:message code="judgment.date" />:</span><span class="date"><joda:format value="${judgment.judgmentDate}" pattern="${DATE_PATTERN}" /></span></div>
				
				<c:if test="${!empty judgment.ccCourtName || !empty judgment.ccCourtDivisionName}" >
				
					<div class="court-desc">
						<span class="court"><c:out value="${judgment.ccCourtName}" /></span>
						 - 
						<span class="division" ><c:out value="${judgment.ccCourtDivisionName}" /></span>
					</div>
				
				</c:if>
				
				<c:if test="${!empty judgment.scCourtDivisionsChamberName || !empty judgment.scCourtDivisionName}" >
				
					<div class="court-desc">
						<span class="chamber">
							<c:out value="${judgment.scCourtDivisionsChamberName}" />
						</span>
						<span>-</span>
						<span class="chamber-division" ><c:out value="${judgment.scCourtDivisionName}" /></span>
					</div>
				
				</c:if>
				
				<c:if test="${!empty judgment.judges}" >
					<div class="judges" >
						<span>
							<spring:message code="judgment.judges" />
							<c:if test="${!empty judgment.scPersonnelType && judgment.scPersonnelType != null}" >
								<div class="personnel-type" >
									<saos:enum value="${judgment.scPersonnelType}" />
								</div>
							</c:if>
							:
						</span>
						<c:forEach items="${judgment.judges}" var="judge" >
							<div class="judge"><c:out value="${judge.name}" /></div>
						</c:forEach>	
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

