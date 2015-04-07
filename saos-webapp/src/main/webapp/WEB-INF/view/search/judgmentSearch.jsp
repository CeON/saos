<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<c:set var="judgments" value="${searchResults.results}" />
<c:set var="resultsNo" value="${searchResults.totalResults}" />
<c:set var="pageNo" value="${pageable.pageNumber}" />
<c:set var="pageSize" value="${pageable.pageSize}" />
<c:set var="sortProperty" value="${pageable.sort.iterator().next().property}" />
<c:set var="sortDirection" value="${pageable.sort.iterator().next().direction}" />

<%-- Cookie that checks if filter box should be visible--%>
<c:set var="showFilterBox" value="" />
  
 <c:forEach var="cookies" items="${cookie}">
    <c:if test="${cookies.value.name == 'saos-filterbox-show' }" >
    	<c:set var="showFilterBox" value="${cookies.value.value}" />
   	</c:if>
 </c:forEach>
  
 <c:set var="resultsListClass" value="" />
 <c:if test="${showFilterBox == 'false'}" >
 	<c:set var="resultsListClass" value="width-full" />
 </c:if> 

<script>
$(document).ready(function() {
	jsInitInJudgmentSearch();
});
</script>



<div class="container search-results">

	<%@ include file="judgmentSearchForm.jsp" %>
	
	<saos:pagePagination pageLink="${pageLink}" pageNo="${pageNo}" totalPages="${totalPages}" ></saos:pagePagination>
	
	<div class="container judgment-list">
	
		<div class="col-xs-3 float-right" >
			<%@ include file="filterBox.jsp" %>
		</div>
	
		<div id="judgment-list" class="col-xs-9 ${resultsListClass}">	
		
			<%@ include file="judgmentList.jsp" %>
		
			<c:if test="${resultsNo == 0}">
				<div class="no-results">
					<p>
					   <spring:message code="judgmentSearch.results.noRecords" />
					</p>
					<p>
					   <spring:message code="judgmentSearch.results.hints" />:
					</p>
					<ul>
					   <li><spring:message code="judgmentSearch.results.hints.first" />,</li>
					   <li><spring:message code="judgmentSearch.results.hints.second" />.</li>
					</ul>
					
				</div>
			</c:if>
		
		</div>
		
	</div>
	
	<saos:pagePagination pageLink="${pageLink}" pageNo="${pageNo}" totalPages="${totalPages}" ></saos:pagePagination>

</div>

