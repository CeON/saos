<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<jsp:useBean id="currentDate" class="java.util.Date" />
<fmt:formatDate value="${currentDate}" pattern="yyyy" var="currentYear"/>

<form:form id="analysisForm" class="form-horizontal" role="form" modelAttribute="analysisForm" action="${contextPath}/analysis" method="GET">


    <fieldset>
    
        <legend><spring:message code="analysis.filters.title"/></legend>

        <div class="form-group">
	
	       <div class="col-sm-12">
	
			    <spring:message code='analysis.filters.phrase.placeholder' var="placeholder"/>
		
			    <c:forEach items="${analysisForm.filters}" var="seriesSearchCriteria" varStatus="status">
				    
				    <div class="form-group" id="seriesSearchPhraseDiv_${status.index}">
				    
				         <div class = "col-xs-8 col-sm-5">
				            <div class="input-group">
	
	                             <span class="input-group-addon" id="inputColourBox_${status.index}"></span>
			                
			                     <form:input path="filters[${status.index}].phrase" class="form-control" id="seriesSearchPhraseInput_${status.index}" placeholder="${placeholder}"/>
			                
			                     <c:if test="${fn:length(analysisForm.filters) > 1}">
			                     
			                         <span class="input-group-addon">
	                                     <img id="deletePhraseButton_${status.index}" tabindex="0" data-toggle="tooltip" style="cursor: pointer;" title="<spring:message code='analysis.button.deletePhrase'/>" src="${contextPath}/static/image/icons/close.png"/>
	                                </span>
	                             
	                             </c:if>
			              </div>
			             </div>
			        </div>
			    </c:forEach>
		  
		    </div>
		    
	    </div>

        <div class="col-sm-12 form-group">
                <input id="addPhraseButton" type="button" class="btn btn-default" value="<spring:message code='analysis.button.addPhrase'/>"/>
        </div>


    </fieldset>        
      
      
    
    <fieldset>
    
        <legend><spring:message code="analysis.xsettings.title"/></legend>
        
          
        <div class="form-group">
	        
	        <div class="form-inline">
	        
	                   <div id="dateRangeInputs" class="col-xs-8 col-sm-12 col-md-9 col-lg-8">
		                   <div class="form-group col-xs-12 col-sm-7">
		                           <label class="control-label"><spring:message code="analysis.xsettings.dateRange"/></label>
		                           <label class="control-label col-sm-offset-1"><spring:message code="from"/></label>
		                           <saos:monthSelect path="xsettings.monthStart" id="xRangeMonthStart"/>
		                           <saos:yearSelect path="xsettings.yearStart" id="xRangeYearStart" yearRange="${currentYear}-1970, 1960, 1950, 1940, 1930"/>
		                   </div>
		                   <div class="form-group col-xs-12 col-sm-5">
	                                <label class="control-label"><spring:message code="to"/></label>
	                                <saos:monthSelect path="xsettings.monthEnd" id="xRangeMonthEnd"/>
	                                <saos:yearSelect path="xsettings.yearEnd" id="xRangeYearEnd" yearRange="${currentYear}-1970, 1960, 1950, 1940, 1930"/>
	                       </div>
                       </div>
	               
	        
        </div>
        
        
        
      </div> 
      
      </fieldset>  
      
      <fieldset>
    
        <legend><spring:message code="analysis.ysettings.title"/></legend>
        
      </fieldset>
        
        
	  <div class="form-group col-sm-12">
          <button type="submit" class="btn btn-primary"><spring:message code="analysis.button.generateChart" /></button>
	  </div>
        
        


</form:form>