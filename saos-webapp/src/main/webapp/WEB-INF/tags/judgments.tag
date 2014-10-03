<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.Collection"%>

<c:forEach var="judgment" items="${items}" >

	<div class="judgment row" >
	
		<div class="row" >
				<c:if test="${!empty judgment.judgmentType && judgment.judgmentType != null}" >
					<c:set var="lowerCaseJudgmentType" value="${fn:toLowerCase(judgment.judgmentType)}" />
					<div class="type" judgmenttype="${lowerCaseJudgmentType}" ><spring:message code="judgment.judgmenttype.${fn:toLowerCase(judgment.judgmentType)}" /></div>
				</c:if>
				<div class="case-number">
					<a href="${contextPath}/result/${judgment.id}">
						<saos:caseNumber items="${judgment.caseNumbers}"/>
					</a>
				</div>
				
				<div class=""><span><spring:message code="judgment.results.date" />:</span><span class="date"><joda:format value="${judgment.judgmentDate}" pattern="dd-MM-yyyy" /></span></div>
				<div class="court-desc"><span class="court"><c:out value="${judgment.courtName}" /></span> - <span class="division" ><c:out value="${judgment.courtDivisionName}" /></span></div>
				
				<div class="judges" >
					<span><spring:message code="judgment.results.judges" />:</span>
					<c:forEach items="${judgment.judges}" var="judge" >
						<div class="judge"><c:out value="${judge}" /></div>
					</c:forEach>	
				</div>
				
				<c:if test="${!empty judgment.keywords}" >
					<div class="keywords">	
						<c:forEach items="${judgment.keywords}" var="keyword" >
							<div class="keyword"><c:out value="${keyword}" /></div>
						</c:forEach>
					</div>
				</c:if>

				<div class="extract">[26] Les activités de soutien au programme OLO, l'accompagnement de personnes à l'hôpital, les paniers de Noël, la popote roulante, le centre de jour ont comme cause principale   […]   Celles-ci sont le soutien au programme OLO, l'accompagnement de personnes à l'hôpital, les paniers de Noël, la popote roulante et le centre de jour.  […]   De plus, le Cercle s'occupe de recueillir des fonds pour le programme OLO (oeuf/lait/orange) pour les femmes enceintes;</div>
				
		</div>

	</div>

</c:forEach>