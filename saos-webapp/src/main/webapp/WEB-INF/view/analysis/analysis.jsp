<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<script>
$(document).ready(function() {
	
    analysisJsProperties = {
            ZOOM_CANCEL_HINT: '<spring:message code="chart.zoomCancelHint"/>'
    }
    
	initAnalysisJs();
	
});
</script>


<div class="container search-page block" id="analysis">
	
	<div class="form-group" id="analysisFormDiv">
	
	       <%@ include file="analysisForm.jsp" %>
	
	</div>
	
	<div class="panel panel-default" style="height: 330px; border-width: 2px;">
	<div class="col-sm-9" style="margin-top: 20px;">
	    <span class="small" id="mainChartZoomCancelHint"></span>
	    <div id="mainChart" style="width: 100%; height: 300px;">
	    </div>
	</div>
    
    <div class="col-sm-3" style="margin-top: 20px;">
        <span class="small" id="aggregatedMainChartZoomCancelHint"></span>
        <div id="aggregatedMainChart" style="width: 100%; height: 180px;">
        </div>
    </div>

     <img id="ajaxChartLoaderImg" src="${contextPath}/static/image/icons/ajax-loader.gif" alt="<spring:message code='analysis.button.ajaxLoader.iconAlt'/>" style="visibility: hidden;"/>
    </div>

</div>
