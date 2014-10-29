<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<c:set var="judgments" value="${resultSearch.results}" />
<c:set var="resultsNo" value="${resultSearch.totalResults}" />
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

<%@ include file="judgmentSearchForm.jsp" %>

<saos:pagePagination pageLink="${pageLink}" pageNo="${pageNo}" totalPages="${totalPages}" ></saos:pagePagination>

<div class="container judgment-list">

	<div class="col-md-9 ${resultsListClass}">	
	
		<saos:judgments items="${judgments}" />
	
		<c:if test="${resultsNo == 0}">
			<spring:message code="judgment.results.noRecords" />
		</c:if>
	
	</div>
	
	<div class="col-md-3">
		<%@ include file="filterBox.jsp" %>
	</div>
	
</div>

<saos:pagePagination pageLink="${pageLink}" pageNo="${pageNo}" totalPages="${totalPages}" ></saos:pagePagination>
