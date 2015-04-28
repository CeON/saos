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
	jsInitInJudgmentSearch();
});
</script>



<div class="container search-results">

	<%@ include file="judgmentSearchForm.jsp" %>
	
	<saos:pagePagination pageLink="${pageLink}" pageNo="${pageNo}" pageSize="${pageSize}" resultsNo="${resultsNo}" ></saos:pagePagination>
	
	<div class="container judgment-list">
	
		<div id="judgment-list" class="col-xs-12 ">	
		
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
	
	<saos:pagePagination pageLink="${pageLink}" pageNo="${pageNo}" pageSize="${pageSize}" resultsNo="${resultsNo}" ></saos:pagePagination>

</div>

