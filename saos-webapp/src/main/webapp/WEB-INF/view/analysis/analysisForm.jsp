<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<jsp:useBean id="currentDate" class="java.util.Date" />
<fmt:formatDate value="${currentDate}" pattern="yyyy" var="currentYear"/>

<form:form id="analysisForm" class="form-horizontal" role="form" modelAttribute="analysisForm" action="${contextPath}/analysis" method="GET">

    
    
    <%--------------------------- GLOBAL CONTEXT --------------------------------%>
   
       <div class="form-group form-group-sm" style="background-color: #dddddd; padding: 10px;">
	
	            
            <div class="col-xs-12">
                <spring:message code="analysis.criteria.judgmentDateRange"/>:
                <span id="judgmentDateRangeInfoDiv" style="cursor: pointer;"></span>
               
	        </div>  
	        
	        <div id="judgmentDateRangeSelectDiv" class="form-inline col-xs-12" style="display:none;">
               <div id="dateRangeInputs" class="form-inline col-xs-10 col-sm-10 col-md-9 col-lg-8">
                   <div class="form-group col-xs-8 col-sm-6 col-md-5">
                       <spring:message code="analysis.criteria.judgmentDateStartMonth.title" var="judgmentDateStartMonthTitle" />
                       <spring:message code="analysis.criteria.judgmentDateStartYear.title" var="judgmentDateStartYearTitle" />
                       <label class="control-label"><spring:message code="from"/></label>
                       <saos:monthSelect path="globalFilter.judgmentDateRange.startMonth" id="judgmentDateStartMonth" title="${judgmentDateStartMonthTitle}" />
                       <saos:yearSelect path="globalFilter.judgmentDateRange.startYear" id="judgmentDateStartYear" yearRange="${currentYear}-1970, 1960, 1950, 1940, 1930"  title="${judgmentDateStartYearTitle}" />
                   </div>
    
                   <div class="form-group col-xs-8 col-sm-6">
                       <spring:message code="analysis.criteria.judgmentDateEndMonth.title" var="judgmentDateEndMonthTitle" />
                       <spring:message code="analysis.criteria.judgmentDateEndYear.title" var="judgmentDateEndYearTitle" />
                       <label class="control-label"><spring:message code="to"/></label>
                       <saos:monthSelect path="globalFilter.judgmentDateRange.endMonth" id="judgmentDateEndMonth" title="${judgmentDateEndMonthTitle}"/>
                       <saos:yearSelect path="globalFilter.judgmentDateRange.endYear" id="judgmentDateEndYear" yearRange="${currentYear}-1970, 1960, 1950, 1940, 1930" title="${judgmentDateEndYearTitle}" />
                   </div>

               </div>
	     </div>
	     </div>
	
 
      
    <%--------------------------- SEARCHED PHRASES --------------------------------%>
   
      
     <div class="form-group form-group-sm" style="background-color: #f7f7f7; padding: 10px;">
	
	       <div class="col-sm-12">
	
			    <spring:message code='analysis.filters.phrase.placeholder' var="placeholder"/>
			    <spring:message code='analysis.filters.phrase.placeholder' var="searchPhraseTitle"/>
		
			    <c:forEach items="${analysisForm.seriesFilters}" var="seriesFilter" varStatus="status">
				    
				    <div class="col-sm-12 col-md-6 col-lg-4 form-group" id="seriesSearchPhraseDiv_${status.index}">
				    
				         <div class = "col-xs-8 col-sm-7 col-md-11 col-lg-12">
				            <div class="input-group">
	
	                             <span class="input-group-addon" id="inputColourBox_${status.index}"></span>
			                
			                     <form:input path="seriesFilters[${status.index}].phrase" class="form-control" id="seriesSearchPhraseInput_${status.index}" placeholder="${placeholder}" title="${searchPhraseTitle}" />
			                
			                     <c:if test="${fn:length(analysisForm.seriesFilters) > 1}">
			                     
			                       	<span class="input-group-addon">
			                             <a id="deletePhraseButton_${status.index}" href="" data-toggle="tooltip" title="<spring:message code='analysis.button.deletePhrase'/>" >
	                                          <img style="cursor: pointer;"  src="${contextPath}/static/image/icons/close.png" alt="<spring:message code='analysis.button.deletePhrase.iconAlt'/>" />
	                                     </a>
		                            </span>
	                             </c:if>
			              </div>
			             </div>
			        </div>
			    </c:forEach>
		  
            <c:if test="${fn:length(analysisForm.seriesFilters)<maxNumberOfSearchPhrases}">
                
                <div class="col-sm-12">
                    <input id="addPhraseButton" type="button" class="btn btn-default btn-sm" value="<spring:message code='analysis.button.addPhrase'/>"/>
                
                </div>
            </c:if>
		    </div>
	    </div>

        


      
      <%--------------------------- Y AXIS --------------------------------%>
      
      
              
       <div class="form-group form-group-sm" style="background-color: #f9f9f9; padding: 10px;">
           <div class="form-inline col-xs-12">
                <div class="col-xs-12">
                <label class="control-label"><spring:message code="analysis.ysettings.yaxis.label"/></label>
                <form:select id="yaxisValueType" path="ysettings.valueType" class="form-control">
                      <saos:enumOptions enumType="pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType" selected="${analysisForm.ysettings.valueType}"/>
                </form:select>
                </div>
           </div>
       </div>
                
        
	 

</form:form>