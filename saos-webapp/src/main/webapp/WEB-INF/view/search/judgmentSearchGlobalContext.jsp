<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div >

    <%-- COURT CRITERIA --%>
    <%@ include file="../common/search/courtCriteriaInfoSection.jsp" %>
    <c:set var="courtCriteriaNestedPath" value="courtCriteria"/>
    <c:set var="courtCriteria" value="${judgmentCriteriaForm.courtCriteria}"/>
    <%@ include file="../common/search/courtCriteriaFormSection.jsp" %>
    
    
    <%-- DATA FORM SECTION --%>
    <div class="info-section-header" >
        <spring:message code="context.date.fieldDescription" />: 
    </div>
    
    <a id="date-info-section" class="info-section" href="" >
        <b><spring:message code="context.date.anyValue" /></b>
    </a>
        
    <div id="date-form-section" class="row form-section">
        
        <spring:message code="judgmentSearch.formField.datePlaceholder" var="datePlaceholder" />
    
        <%-- Date selection --%>
        <div class="form-group ">
            
            <div class="col-lg-6 col-md-12 col-sm-6 col-xs-12">
                <label for="datepicker_from" ><spring:message code="judgmentSearch.formField.dateFrom" />:</label>
                <form:input path="dateFrom" class="form-control form-date" id="datepicker_from" placeholder="${datePlaceholder}" maxlength="10" autocomplete="off" />
                <label id="datepicker_from-error" class="valid-error" for="datepicker_from"><spring:message code="judgmentSearch.validation.date.error" /></label>
                <label id="datepicker_from-desc" class="for-screen-readers" for="datepicker_from"><spring:message code="judgmentSearch.formField.dateWrongFormat" /></label>
            </div>
            <div class="col-lg-6 col-md-12 col-sm-6 col-xs-12">
                <label for="datepicker_to" ><spring:message code="judgmentSearch.formField.dateTo" />:</label>
                <form:input path="dateTo" class="form-control form-date" id="datepicker_to" placeholder="${datePlaceholder}" maxlength="10" autocomplete="off" />
                <label id="datepicker_to-error" class="valid-error" for="datepicker_to"><spring:message code="judgmentSearch.validation.date.error" /></label>
                <label id="datepicker_to-desc" class="for-screen-readers" for="datepicker_to"><spring:message code="judgmentSearch.formField.dateWrongFormat" /></label>
            </div>
        </div>
        
        <div class="align-right">
            <button id="confirm-section-date" class="btn btn-xs button"><spring:message code="confirm" /></button>
        </div>
    
    </div>
    
</div>

