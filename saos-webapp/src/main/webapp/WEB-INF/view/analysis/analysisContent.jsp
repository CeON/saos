<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%-- TODO: REMOVE IT AFTER https://github.com/CeON/saos/issues/800 --%>
<style>
<!--
    #ccCourtChart * .flot-x-axis .flot-tick-label {
        font-size: 9px;
    }
-->
</style>

<script>
$(document).ready(function() {
	
    analysisJsProperties = {
            ZOOM_CANCEL_HINT: '<spring:message code="chart.zoomCancelHint"/>',
            PAGE_LANG: '<spring:message code="page.lang"/>'
    }
    
	initAnalysisJs();
	
});
</script>

	
<div>
    <h2><spring:message code="analysis.chart.header" /></h2>

	<a id="exportToCsv-MAIN_CHART" class="export-csv" style="float:right" href="" data-toggle="tooltip" data-placement="left" title="<spring:message code='analysis.button.exportToCsv'/>" >
	    <img style="cursor: pointer;"  src="${contextPath}/static/image/icons/exportCsv.png" alt="<spring:message code='analysis.button.exportToCsv.iconAlt'/>" />
	</a>
</div>

<div class="clearfix"></div>
  

<div class="panel panel-default" style="border-width: 2px;">

    <div class="panel-body">
    

		<div class="col-xs-12 col-sm-9">
		    <span class="small" id="mainChartZoomCancelHint"></span>
		    <div id="mainChart" style="width: 100%; height: 300px;">
		    </div>
		</div>
	   
		<div class="col-xs-5 col-sm-3">
		    <span class="small" id="aggregatedMainChartZoomCancelHint"></span>
		    <div id="aggregatedMainChart" style="width: 100%; height: 180px;">
		    </div>
		</div>

   </div>

</div>
   
   
  
  
<div id="ccCourtChartDiv" style="margin-top: 40px;">

	<div>
	    <a id="exportToCsv-CC_COURT_CHART" class="export-csv" style="float:right" href="" data-trigger="click" data-toggle="tooltip" data-placement="left" title="<spring:message code='analysis.button.exportToCsv'/>" >
	        <img style="cursor: pointer;"  src="${contextPath}/static/image/icons/exportCsv.png" alt="<spring:message code='analysis.button.exportToCsv.iconAlt'/>" />
	    </a>
	 </div>
	 
	 <div class="clearfix"></div>
	
	 <div id="ccCourtChartPanel" class="panel panel-default" style="border-width: 2px; ">
	    <div class="panel-body">
	 
	  <div class="col-xs-12 col-sm-12">
	      <span class="small" id="ccCourtChartZoomCancelHint"></span>
	      <div id="ccCourtChart" style="width: 100%; height: 300px;">
	      </div>
	   </div>
	 
	     </div>
	</div>

</div>


<img id="ajaxChartLoaderImg" src="${contextPath}/static/image/icons/ajax-loader.gif" alt="<spring:message code='analysis.button.ajaxLoader.iconAlt'/>" style="visibility: hidden;"/>
