<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<script>
$(document).ready(function() {
	new initAnalysisJs().initButtonActions();
});
</script>


<div class="container judgment-page block" id="analysis">

<form:form id="analysisForm" class="form-horizontal" role="form" modelAttribute="analysisForm" action="${contextPath}/analysis" method="GET">

<div class="container block">


    <fieldset id="analysisFieldset" >
        
        <div class="form-group" id="analysisSearchCriteriaDiv">
	        <%@ include file="analysisSearchCriteria.jsp" %>
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
</div>
