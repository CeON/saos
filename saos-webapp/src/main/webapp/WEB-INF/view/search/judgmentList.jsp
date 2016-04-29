<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<ol>
<c:forEach var="judgment" items="${judgments}" >

    <li class="judgment row" >


        <%-- Top line --%>
        <div class="top-line">
            <div class="top-line-main">
                <c:if test="${!empty judgment.judgmentType && judgment.judgmentType != null}" >
                
                    <a href="" class="judgment-type" data-filter-value="${judgment.judgmentType}" data-toggle="tooltip" title="<spring:message code="judgmentSearch.filter.add"/>" >
                        <saos:enum value="${judgment.judgmentType}" ></saos:enum>
                    </a>

                </c:if>
            
                <c:if test="${!empty judgment.courtType && judgment.courtType != null}" >
                    <span>-</span>
                    <a href="" class="court-type" data-filter-value="${judgment.courtType}" data-toggle="tooltip" title="<spring:message code="judgmentSearch.filter.set"/>"><spring:message code="judgmentSearch.results.courtType.${judgment.courtType}" /></a>
                </c:if>
            
            </div>
            
            <div class="top-line-right">
                <div>
                    <c:choose>
                        <c:when test="${judgment.referencingCount>0}">
                            <a href="${contextPath}/search?referencedCourtCaseId=${judgment.id}" title="<spring:message code="judgmentSearch.results.referencingCount.link" />">
                                <spring:message code="judgmentSearch.results.referencingCount" arguments="${judgment.referencingCount}" />
                            </a>
                        </c:when>
                        <c:otherwise>
                            <spring:message code="judgmentSearch.results.referencingCount" arguments="${judgment.referencingCount}" />
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${!empty judgment.maxMoneyAmount}"><div><spring:message code="judgmentSearch.results.maxMoneyAmount" />: <fmt:formatNumber value="${judgment.maxMoneyAmount}" maxFractionDigits="2" /> zł</div></c:if>
            </div>
        </div>
    
        <div class="case-number">
            <h2>
                <a href="${contextPath}/judgments/${judgment.id}" title="<spring:message code="judgmentSearch.results.link"  />" >
                    <saos:caseNumber items="${judgment.caseNumbers}"/>
                </a>
            </h2>
        </div>
        
        <div class="">
          <span><spring:message code="judgment.date" />:</span>
          <a href="" class="date" data-toggle="tooltip" title="<spring:message code="judgmentSearch.filter.set"/>">
              <joda:format value="${judgment.judgmentDate}" pattern="${DATE_PATTERN}" />
          </a>
        </div>
        
        <%-- CommonCourt & CommonCourtDivision  --%>
        <c:if test="${!empty judgment.ccCourtName || !empty judgment.ccCourtDivisionName}" >
        
            <div class="court-desc">                    
                <saos:propertyWithFilter value="${judgment.ccCourtName}" filterValue="${judgment.ccCourtId}" cssClass="court" />
                <span>-</span>
                <span class="not-link-division">${judgment.ccCourtDivisionName}</span>
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
                            <a href="" class="judge" data-toggle="tooltip" data-filter-value="&quot;${fn:escapeXml(fn:replace(judge.name, '\"', ' '))}&quot;" title="<spring:message code="judgmentSearch.filter.set"/>" ><c:out value="${judge.name}" /></a>
                            
                            <c:if test="${judge.presidingJudge}" >
                                <span class="presiding-judge-dark"  data-toggle="tooltip" title="<spring:message code="judgment.judgeRole.PRESIDING_JUDGE" />" >
                                </span>
                            </c:if>
                        </li>
                    </c:forEach>
                </ul>    
            </div>
        </c:if>
        
        <%-- SupremeCourtJudgmentForm --%>
        <c:if test="${!empty judgment.scJudgmentFormId && !empty judgment.scJudgmentFormName}" >
            <div class="" >
                <span>
                    <spring:message code="judgment.scJudgmentForm" />:
                </span>
                <a href="" class="judgment-form" data-toggle="tooltip" title="<spring:message code="judgmentSearch.filter.set"/>" data-filter-value="${judgment.scJudgmentFormId}" >
                    <c:out value="${judgment.scJudgmentFormName}" />
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
                        <li ><a href="" class="keyword" data-toggle="tooltip" title="<spring:message code="judgmentSearch.filter.add"/>" ><c:out value="${keyword}" /></a></li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <p class="extract">${judgment.content}</p>
    
    </li>

</c:forEach>
</ol>
