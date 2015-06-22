<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<script>
$(document).ready(function() {
	
    analysisJsProperties = {
            ZOOM_CANCEL_HINT: '<spring:message code="chart.zoomCancelHint"/>',
            PAGE_LANG: '<spring:message code="page.lang"/>',
            CHART_TABLE_NULL_PHRASE: '<spring:message code="analysis.chart.table.nullPhrase"/>',
            CHART_TABLE_SEARCHED_PHRASE: '<spring:message code="analysis.chart.table.searchedPhrase"/>'
    }
    
	initAnalysisJs();
	
});
</script>

	
<div>
    <h1><spring:message code="analysis.chart.header" /></h1>

    <div class="form-inline yaxisValueSelect">
        <label class="control-label"><spring:message code="analysis.ysettings.yaxis.label"/></label>
        <select id="yaxisValueType" name="ysettings.valueType" class="form-control">
             <saos:enumOptions enumType="pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType" selected="${analysisForm.ysettings.valueType}"/>
        </select>
    </div>
                
</div>


	<a id="exportToCsv-MAIN_CHART" class="export-csv" style="float:right" href="" data-toggle="tooltip" data-placement="left" title="<spring:message code='analysis.button.exportToCsv'/>">
	    <img style="cursor: pointer;"  src="${contextPath}/static/image/icons/exportCsv.png" alt="<spring:message code='analysis.button.exportMainChartToCsv.aria'/>"/>
	</a>


<div class="clearfix"></div>

<div aria-label="<spring:message code='analysis.mainChart.title'/>">

	<div class="panel panel-default" style="border-width: 2px;" aria-hidden="true">
	
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
	
</div>
   
<table id="mainChartTable" class="table visuallyhidden" tabindex="0">
    <caption><spring:message code='analysis.mainChart.table.caption'/></caption>
    <thead></thead>
    <tbody></tbody>
</table>     
  
<div id="ccCourtChartDiv" style="margin-top: 40px;">

	<div>
	    <a id="exportToCsv-CC_COURT_CHART" class="export-csv" style="float:right" href="" data-toggle="tooltip" data-placement="left" title="<spring:message code='analysis.button.exportToCsv'/>">
	        <img style="cursor: pointer;"  src="${contextPath}/static/image/icons/exportCsv.png" alt="<spring:message code='analysis.button.exportCcCourtChartToCsv.aria'/>"/>
	    </a>
	 </div>
	 
	 <div class="clearfix"></div>
	
	 <div aria-label="<spring:message code='analysis.ccCourtChart.title'/>">
	
		 <div id="ccCourtChartPanel" class="panel panel-default" style="border-width: 2px; " aria-hidden="true">
		    <div class="panel-body">
		 
		  <div class="col-xs-12 col-sm-12">
		      <span class="small" id="ccCourtChartZoomCancelHint"></span>
		      <div id="ccCourtChart" style="width: 100%; height: 300px;">
		      </div>
		   </div>
		 
		     </div>
		</div>
	</div>
	
	<table id="ccCourtChartTable" class="table visuallyhidden" tabindex="0">
        <caption><spring:message code='analysis.ccCourtChart.table.caption'/></caption>
        <thead></thead>
        <tbody></tbody>
    </table>  

</div>





<img id="ajaxChartLoaderImg" src="${contextPath}/static/image/icons/ajax-loader.gif" alt="<spring:message code='analysis.button.ajaxLoader.iconAlt'/>" style="display:none;"/>
<div id="simpleChartPointTooltip" class="chart-point-tooltip" style="display:none;"></div>

<div id="mainChartPointTooltip" class="chart-point-tooltip" style="display:none" >
    <div>
        <span><spring:message code="analysis.chart.tooltip.timePeriod"/>: </span><b><span id="pointTimePeriod"></span></b>
    </div>
    <div>
        <span><spring:message code="analysis.chart.tooltip.searchedPhrase"/>: </span><b><span id="pointSearchedPhrase"></span></b>
    </div>
    <div>
        <span><spring:message code="analysis.chart.tooltip.judgmentCount"/>: </span><b><span id="pointJudgmentCount"></span></b>
    </div>
    <div id="pointSearchLinkDiv" class="point-search-link">    
        <a id="pointSearchLink" href=""><spring:message code="analysis.chart.tooltip.searchLink"/></a>
    </div>
</div>

<div id="aggregatedMainChartPointTooltip" class="chart-point-tooltip" style="display:none; width: 230px;">
    <div>
        <span><spring:message code="analysis.chart.tooltip.timePeriod"/>: </span><b><span id="pointTimePeriod"></span></b>
    </div>
    <div>
        <span><spring:message code="analysis.chart.tooltip.searchedPhrase"/>: </span><b><span id="pointSearchedPhrase"></span></b>
    </div>
    <div>
        <span><spring:message code="analysis.chart.tooltip.judgmentCount"/>: </span><b><span id="pointJudgmentCount"></span></b>
    </div>
    <div id="pointSearchLinkDiv" class="point-search-link">    
        <a id="pointSearchLink" href=""><spring:message code="analysis.chart.tooltip.searchLink"/></a>
    </div>
</div>

<div id="ccCourtChartPointTooltip" class="chart-point-tooltip" style="display:none">
    <div>
        <b><span id="pointCourtName"></span></b>
    </div>
    <div>
        <span><spring:message code="analysis.chart.tooltip.timePeriod"/>: </span><b><span id="pointTimePeriod"></span></b>
    </div>
    
    <div>
        <span><spring:message code="analysis.chart.tooltip.searchedPhrase"/>: </span><b><span id="pointSearchedPhrase"></span></b>
    </div>
    
    <div>
        <span><spring:message code="analysis.chart.tooltip.judgmentCount"/>: </span><b><span id="pointJudgmentCount"></span></b>
    </div>
    <div id="pointSearchLinkDiv" class="point-search-link">    
        <a id="pointSearchLink" href=""><spring:message code="analysis.chart.tooltip.searchLink"/></a>
    </div>
</div>
