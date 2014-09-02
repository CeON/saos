<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="container judgment-list">

	
	<div class="judgment-filter block row" >
		<h3><spring:message code="judgment.results.header" /></h3>
		<div>Znaleziono <strong>4</strong> orzeczenia </div>
		<div>Sortuj po: 
			<select>
			<option>dacie</option>
			<option>trafności</option>
			</select>		
		</div>
	</div>


	<c:forEach var="judgment" items="${judgments}" >
	
		<div class="judgment row" >
		
			<div class="row" >
				<div class="col-md-8">
					<div class="type" ><span><spring:message code="judgment.results.${fn:toLowerCase(judgment.judgmentType)}" /></span></div>
					<div class="case-number"><a href="${contextPath}/result/${judgment.id}"><c:out value="${judgment.caseNumber}" /></a></div>
					
					<div class="date"><span><spring:message code="judgment.results.date" />:</span><c:out value="${judgment.judgmentDate}" /></div>
					<div class="court"><c:out value="${judgment.courtDivision.court.name}" /> - <c:out value="${judgment.courtDivision.name}" /></div>
					
					
					<div class="judges" >
						<span><spring:message code="judgment.results.judges" />:</span>
						<c:forEach items="${judgment.judges}" var="judge" >
							<div class="judge"><c:out value="${judge.name}" /></div>
						</c:forEach>	
					</div>
					
					<!--  <h4>Słowa kluczowe:</h4>  -->
					
					<c:if test="${!empty judgment.keywords}" >
						<div class="keywords">	
							<c:forEach items="${judgment.keywords}" var="keyword" >
								<div class="keyword"><c:out value="${keyword.phrase}" /></div>
							</c:forEach>
						</div>
					</c:if>
				</div>
				<div class="col-md-4">
					<div class="accuracy" >99%</div>
				</div>
				
			</div>
			
			<div class="row">
				<div class="col-md-12" >
					<div class="extract">[26] Les activités de soutien au programme OLO, l'accompagnement de personnes à l'hôpital, les paniers de Noël, la popote roulante, le centre de jour ont comme cause principale   […]   Celles-ci sont le soutien au programme OLO, l'accompagnement de personnes à l'hôpital, les paniers de Noël, la popote roulante et le centre de jour.  […]   De plus, le Cercle s'occupe de recueillir des fonds pour le programme OLO (oeuf/lait/orange) pour les femmes enceintes;</div>
					
				</div>
			</div>

		</div>

	</c:forEach>


</div>
