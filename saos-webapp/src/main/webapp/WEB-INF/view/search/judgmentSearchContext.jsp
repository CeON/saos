<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="search-context" data-context-form-field="referencedCourtCaseId">

    <h2 class="info-section-header" >
        <spring:message code="judgmentSearch.context.referencedJudgment.text" />:
    </h2>
    
    <div class="referenced-regulations">
        
        <a href="${contextPath}/judgments/${referencedJudgment.id}" title="<spring:message code='judgmentSearch.context.referencedJudgment.details.link' />" >
            <saos:caseNumber items="${referencedJudgment.caseNumbers}"/>
        </a>
        
        <a href="" class="remove-button remove-search-context remove" data-toggle="tooltip" data-placement="top" title="<spring:message code='judgmentSearch.context.referencedJudgment.remove' />" >
        </a>
        
        <form:hidden path="referencedCourtCaseId" id="referencedCourtCaseId" />
        
    </div>
    
</div>


