<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="form-section-container" >

    <div class="context-bar" >
       <%@ include file="../common/search/courtCriteriaInfoSection.jsp" %>
        
        <a id="date-info-section" class="info-section" href="" >
           <spring:message code="context.date.fieldDescription" />: <b><spring:message code="context.date.anyValue" /></b>
        </a>
    </div>
    
    <c:set var="courtCriteriaNestedPath" value="courtCriteria"/>
    <c:set var="courtCriteria" value="${judgmentCriteriaForm.courtCriteria}"/>
    <%@ include file="../common/search/courtCriteriaFormSection.jsp" %>
    
    <%-- DATA FORM SECTION --%>
    <div id="date-form-section" class="row form-section">
        
        <spring:message code="judgmentSearch.formField.datePlaceholder" var="datePlaceholder" />
    
        <%-- Date selection --%>
        <div class="form-group ">
            <label for="datepicker_from" class="col-lg-2 col-md-3 col-sm-3 col-xs-12 control-label"><spring:message code="judgmentSearch.formField.dateFrom" />:</label>
            <div class="col-lg-2 col-md-2 col-sm-9 col-xs-12">
                <label id="datepicker_from-error" class="valid-error" for="datepicker_from"><spring:message code="judgmentSearch.validation.date.error" /></label>
                <form:input path="dateFrom" class="form-control form-date" id="datepicker_from" placeholder="${datePlaceholder}" maxlength="10" autocomplete="off" />
                <label id="datepicker_from-desc" class="for-screen-readers" for="datepicker_from"><spring:message code="judgmentSearch.formField.dateWrongFormat" /></label>
            </div>
        
            <label for="datepicker_to" class="col-lg-2 col-md-2 col-sm-3 col-xs-12 control-label"><spring:message code="judgmentSearch.formField.dateTo" />:</label>
            <div class="col-lg-2 col-md-2 col-sm-9 col-xs-12">
                <label id="datepicker_to-error" class="valid-error" for="datepicker_to"><spring:message code="judgmentSearch.validation.date.error" /></label>
                <form:input path="dateTo" class="form-control form-date" id="datepicker_to" placeholder="${datePlaceholder}" maxlength="10" autocomplete="off" />
                <label id="datepicker_to-desc" class="for-screen-readers" for="datepicker_to"><spring:message code="judgmentSearch.formField.dateWrongFormat" /></label>
            </div>
        </div>
    
    </div>
    
</div>

