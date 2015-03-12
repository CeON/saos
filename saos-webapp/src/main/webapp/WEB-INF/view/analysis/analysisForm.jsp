<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<jsp:useBean id="currentDate" class="java.util.Date" />
<fmt:formatDate value="${currentDate}" pattern="yyyy" var="currentYear"/>

<form:form id="analysisForm" class="form-horizontal" role="form" modelAttribute="analysisForm" action="${contextPath}/analysis" method="GET">

<div class="container block">


    <fieldset id="analysisFieldset" >
        
	    <div class="col-sm-12">
	
		    <spring:message code='analysis.seriesSeachCriteria.phrase.placeholder' var="placeholder"/>
	
		    <c:forEach items="${analysisForm.filters}" var="seriesSearchCriteria" varStatus="status">
		        <div class="col-sm-12">
			        <div class="col-sm-1" id="inputColourBox_${status.index}" style="padding:0px; width: 20px; height: 20px; border:1px solid"></div>
			        <div class="col-sm-4" id="seriesSearchPhraseDiv_${status.index}">
			            <div class="col-sm-10">
			                <form:input path="filters[${status.index}].phrase" class="form-control" id="seriesSearchPhraseInput_${status.index}" placeholder="${placeholder}"/>
			            </div>
			            <c:if test="${fn:length(analysisForm.filters) > 1}">
			                <div class="col-sm-2">
			                    <img id="deletePhraseButton_${status.index}" tabindex="0" data-toggle="tooltip" style="cursor: pointer;" title="<spring:message code='analysis.button.deletePhrase'/>" src="${contextPath}/static/image/icons/close.png"/>
			                </div>
			            </c:if>
			        </div>
		        </div>    
		    </c:forEach>
		  
	    </div>

        <div class="col-sm-12">
             <input id="addPhraseButton" type="button" class="btn btn-primary button button-blue" value="<spring:message code='analysis.button.addPhrase'/>"/>
        </div>
    
      
        
        <div class="col-sm-12" id="daterange">
            <label for="dateRangeInputs" class="col-sm-1 control-label"><spring:message code="analysis.xsettings.dateRange"/></label>
            <div id="dateRangeInputs" class="col-sm-11">
                <label for="dateRangeFrom" class="col-sm-1 control-label"><spring:message code="from"/></label>
                <div class="col-sm-2">
                    <saos:monthSelect path="xsettings.monthStart" id="xRangeMonthStart"/>
                </div>
	            <div class="col-sm-2">
	                <saos:yearSelect path="xsettings.yearStart" id="xRangeYearStart" yearRange="${currentYear}-1970, 1960, 1950, 1940, 1930"/>
                </div>
	        <label for="dateRangeTo" class="col-sm-1 control-label"><spring:message code="to"/></label>
                <div class="col-sm-2">
                    <saos:monthSelect path="xsettings.monthEnd" id="xRangeMonthEnd"/>
	            </div>
	            <div class="col-sm-2">
                    <saos:yearSelect path="xsettings.yearEnd" id="xRangeYearEnd" yearRange="${currentYear}-1970, 1960, 1950, 1940, 1930"/>
                </div>
        </div>
      </div> 
        
        
        
	     <div class="form-group button-group">
	         <div class="col-sm-12">
	             <button type="submit" class="btn btn-primary button button-blue"><spring:message code="analysis.button.generateChart" /></button>
	         </div>
	     </div>
        
        
    </fieldset>

</div>

</form:form>