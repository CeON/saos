<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<script>
$(document).ready(function() {
	
	zoomCancelHint = '<spring:message code="chart.zoomCancelHint"/>'
	mainChartUrl = '<c:url value="/analysis/generate"/>';
    
	initAnalysisJs();
	
});
</script>


<div class="container judgment-page block" id="analysis">

<div class="form-group" id="analysisFormDiv">

       <%@ include file="analysisForm.jsp" %>

</div>


<div class="col-sm-12">
    <span class="small" id="mainChartZoomCancelHint"></span>
    <div id="mainChart" style="width: 100%; height: 400px;">
    </div>
</div>


</div>
