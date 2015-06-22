<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<jsp:useBean id="currentDate" class="java.util.Date" />
<fmt:formatDate value="${currentDate}" pattern="yyyy" var="currentYear"/>

<form:form id="analysisForm" class="form-horizontal" role="form" modelAttribute="analysisForm" action="${contextPath}/analysis" method="GET">

    <fieldset>
        <legend><spring:message code="analysis.header" /></legend>
    
    <%--------------------------- GLOBAL CONTEXT --------------------------------%>
   
   
    <%@ include file="globalContext/analysisGlobalContext.jsp" %>	
 
      
    <%--------------------------- SEARCHED PHRASES --------------------------------%>
   
      
     <div >
	
	       
			    <spring:message code='analysis.filters.phrase.placeholder' var="placeholder"/>
			    <spring:message code='analysis.filters.phrase.placeholder' var="searchPhraseTitle"/>
		
			    <c:forEach items="${analysisForm.seriesFilters}" var="seriesFilter" varStatus="status">
				    
				    <div class="col-sm-6 col-md-11 col-lg-11 form-group no-margin-right" id="seriesSearchPhraseDiv_${status.index}">
				     
				            <div >
	
	                             <span class="input-group-addon input-color" id="inputColourBox_${status.index}"></span>
			                
			                     <form:input path="seriesFilters[${status.index}].phrase" class="form-control" id="seriesSearchPhraseInput_${status.index}" placeholder="${placeholder}" title="${searchPhraseTitle}" />
			                     
			                 </div>
			                 
			                 <c:if test="${fn:length(analysisForm.seriesFilters) > 1}">
	                             <span class="input-group-addon remove-button">
		                             <a id="deletePhraseButton_${status.index}" href="" data-toggle="tooltip" title="<spring:message code='analysis.button.deletePhrase'/>" >
                                         <img style="cursor: pointer;"  src="${contextPath}/static/image/icons/close.png" alt="<spring:message code='analysis.button.deletePhrase.iconAlt'/>" />
		                             </a>
	                             </span>
                             </c:if>
			             
			        </div>
			    </c:forEach>
		  
		  
		  
            <c:if test="${fn:length(analysisForm.seriesFilters) < maxNumberOfSearchPhrases}">
                
                <div class="col-sm-6 col-md-11 col-lg-11 form-group no-margin-right">
                    <span class="input-group-addon input-color"></span>
                    <button id="addPhraseButton" class="form-control input-button">
                        <spring:message code='analysis.button.addPhrase'/>
                    </button>
                
                </div>
            </c:if>
		  
	    </div>

        


      
      <%--------------------------- Y AXIS --------------------------------%>
      
      
      <form:hidden id="yaxisValueTypeHidden" path="ysettings.valueType"/>
              
      
        
	 </fieldset>

</form:form>

