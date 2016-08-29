<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
    
    
<script>
$(document).ready(function() {
    jsInitInJudgmentDetails();
});
</script>    
        
<div class="details container">

    <div class="row row-eq-height" >
	    <div class="navigation col-md-4">
	        
	        <%@ include file="../common/navigationMenu.jsp" %>
	        
	        <%@ include file="../details/judgmentDetailsNavigation.jsp" %>
	        
	    </div>
	
	    <div class="content col-md-8" id="judgment-content" tabindex="-1">
	        
	        <%@ include file="../details/judgmentDetailsContent.jsp" %>
	        
	    </div>
	    
    </div>

    <%@ include file="../template/appInfoFooter.jsp" %>
    
</div>

        
