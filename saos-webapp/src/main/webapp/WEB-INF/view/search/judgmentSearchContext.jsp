<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%--
<legend data-context-form-field="referencedCourtCaseId">
 --%>
 
<div class="info-section-header" >
    <spring:message code="judgmentSearch.context.referencedJudgment.text" />:
</div>
<div class="referenced-regulations">
    
    <a href="${contextPath}/judgments/${referencedJudgment.id}" title="<spring:message code='judgmentSearch.context.referencedJudgment.details.link' />" >
        <saos:caseNumber items="${referencedJudgment.caseNumbers}"/>
    </a>
    
    <a href="" class="remove-button remove-search-context remove" data-toggle="tooltip" data-placement="top" title="<spring:message code='judgmentSearch.context.referencedJudgment.remove' />" >
    </a>
    
</div>
    



