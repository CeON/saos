<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<c:set var="judgments" value="${resultSearch.results}" />
<c:set var="resultsNo" value="${resultSearch.totalResults}" />
<c:set var="pageNo" value="${pageable.pageNumber}" />
<c:set var="pageSize" value="${pageable.pageSize}" />
<c:set var="sortProperty" value="${pageable.sort.iterator().next().property}" />
<c:set var="sortDirection" value="${pageable.sort.iterator().next().direction}" />


<%@ include file="judgmentSearchForm.jsp" %>

<saos:pagePagination pageLink="${pageLink}" pageNo="${pageNo}" totalPages="${totalPages}" ></saos:pagePagination>

<div class="container judgment-list">

	<div class="col-md-9">	
	
		<saos:judgments items="${judgments}" />
	
		<c:if test="${resultsNo == 0}">
			<spring:message code="judgment.results.noRecords" />
		</c:if>
	
	</div>
	
	<div class="col-md-3">
		<saos:filterBox ></saos:filterBox>	
	</div>
	
</div>

<saos:pagePagination pageLink="${pageLink}" pageNo="${pageNo}" totalPages="${totalPages}" ></saos:pagePagination>
