<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

    
 <div id="date-range-form-section" class="row form-section">
      
      <div id="dateRangeInputs" class="form-group form-inline col-xs-12">
      
           <div class="col-xs-12">
               <spring:message code="analysis.criteria.judgmentDateStartMonth.title" var="judgmentDateStartMonthTitle" />
               <spring:message code="analysis.criteria.judgmentDateStartYear.title" var="judgmentDateStartYearTitle" />
               <label class="col-xs-12"><spring:message code="from"/></label>
               <saos:monthSelect path="globalFilter.judgmentDateRange.startMonth" id="judgmentDateStartMonth" title="${judgmentDateStartMonthTitle}" />
               <saos:yearSelect path="globalFilter.judgmentDateRange.startYear" id="judgmentDateStartYear" yearRange="${currentYear}-1970, 1960, 1950, 1940, 1930"  title="${judgmentDateStartYearTitle}" />
           </div>
    
           <div class="col-xs-12">
               <spring:message code="analysis.criteria.judgmentDateEndMonth.title" var="judgmentDateEndMonthTitle" />
               <spring:message code="analysis.criteria.judgmentDateEndYear.title" var="judgmentDateEndYearTitle" />
               <label class="col-xs-12"><spring:message code="to"/></label>
               <saos:monthSelect path="globalFilter.judgmentDateRange.endMonth" id="judgmentDateEndMonth" title="${judgmentDateEndMonthTitle}"/>
               <saos:yearSelect path="globalFilter.judgmentDateRange.endYear" id="judgmentDateEndYear" yearRange="${currentYear}-1970, 1960, 1950, 1940, 1930" title="${judgmentDateEndYearTitle}" />
           </div>

      </div>
      
      <div class="align-right">
          <button id="confirm-section-date-range" class="btn btn-xs button"><spring:message code="confirm" /></button>
      </div>
 </div>