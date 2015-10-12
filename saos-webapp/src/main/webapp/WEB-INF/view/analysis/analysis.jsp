<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
        

<script>
$(document).ready(function() {
    
    analysisJsProperties = {
            ZOOM_CANCEL_HINT: '<spring:message code="chart.zoomCancelHint"/>',
            PAGE_LANG: '<spring:message code="page.lang"/>',
            CHART_TABLE_NULL_PHRASE: '<spring:message code="analysis.chart.table.nullPhrase"/>',
            CHART_TABLE_SEARCHED_PHRASE: '<spring:message code="analysis.chart.table.searchedPhrase"/>',
            MAIN_CHART_TABLE_COL_HEADER_YEAR: '<spring:message code="analysis.chart.table.col.header.year"/>'
    }
    
    initAnalysisJs();
    
});
</script>
        
<div class="analysis container">

    <div class="row row-eq-height" >
        <div class="navigation col-md-4">
            
            <%@ include file="../common/navigationMenu.jsp" %>
            
            <%@ include file="../common/saosLogo.jsp" %>
	           
            <div id="analysis" tabindex="-1">
                <div class="form-group" id="analysisFormDiv">
            
                    <%@ include file="analysisForm.jsp" %>
                    
                </div>
            </div>
        </div>
    
		<div class="content col-md-8" id="content" tabindex="-1">
            
            <%@ include file="analysisContent.jsp" %>
            
        </div>
    </div>

</div>
        
        
        