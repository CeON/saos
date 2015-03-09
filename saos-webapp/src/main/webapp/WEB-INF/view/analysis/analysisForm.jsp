<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>



<form:form id="analysisForm" class="form-horizontal" role="form" modelAttribute="analysisForm" action="${contextPath}/analysis" method="GET">

<div class="container block">


    <fieldset id="analysisFieldset" >
        
	    <div class="col-sm-12">
	
		    <spring:message code='analysis.seriesSeachCriteria.phrase.placeholder' var="placeholder"/>
	
		    <c:forEach items="${analysisForm.filters}" var="seriesSearchCriteria" varStatus="status">
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
		            
		    </c:forEach>
		  
		    <div class="col-sm-3">
		        <input id="addPhraseButton" type="button" class="btn btn-primary button button-blue" value="<spring:message code='analysis.button.addPhrase'/>"/>
		    </div>
	
	    </div>
      
        
        <%--
        <div class="form-group">
            <div class="col-sm-2">
                
                <input id="daterange" maxlength="10" />                 
            
            </div>
        </div>
        --%>
        
	     <div class="form-group button-group">
	         <div class="col-sm-9">
	             <button type="submit" class="btn btn-primary button button-blue"><spring:message code="analysis.button.generateChart" /></button>
	         </div>
	     </div>
        
        
    </fieldset>

</div>

</form:form>