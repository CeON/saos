<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
        

<c:set var="judgments" value="${searchResults.results}" />
<c:set var="resultsNo" value="${searchResults.totalResults}" />
<c:set var="pageNo" value="${pageable.pageNumber}" />
<c:set var="pageSize" value="${pageable.pageSize}" />
<c:set var="sortProperty" value="${pageable.sort.iterator().next().property}" />
<c:set var="sortDirection" value="${pageable.sort.iterator().next().direction}" />


<script>
$(document).ready(function() {
    jsInitInJudgmentSearch({
    	trackFocusOn: "${trackFocusOn}"
    });
});
</script>
        
<div class="search container">

    <form:form id="search-form" class="form-horizontal" role="form" modelAttribute="judgmentCriteriaForm" action="${contextPath}/search" method="GET">

	    <div class="row row-eq-height">
	        
	        <div class="navigation col-md-4">
	                
	            <%@ include file="../common/navigationMenu.jsp" %>
	            
	            <%@ include file="../search/judgmentSearchNavigation.jsp" %>
	                        
	        </div>
	    
	        <div class="content col-md-8" id="judgment-search-content" tabindex="-1" style="margin-bottom: 40px">
	            
	            <%@ include file="../search/judgmentSearchContent.jsp" %>
        
            </div>
	           
	              
	    </div>

        <%@ include file="../template/appInfoFooter.jsp" %>
               
	      
        
    </form:form>
    

    
</div>

        