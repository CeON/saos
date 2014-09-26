<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<c:set var="judgments" value="${resultSearch.results}" />
<c:set var="resultsNo" value="${resultSearch.totalResults}" />
<c:set var="pageNo" value="${pageable.pageNumber}" />
<c:set var="pageSize" value="${pageable.pageSize}" />
<c:set var="sortProperty" value="${pageable.sort.iterator().next().property}" />
<c:set var="sortDirection" value="${pageable.sort.iterator().next().direction}" />


<%@ include file="judgmentSearchForm.jsp" %>

<div class="container judgment-list">

	<div class="block" >
		
	</div>
	
	<saos:judgments items="${judgments}" />

	<c:if test="${resultsNo == 0}">
		<spring:message code="judgment.results.noRecords" />
	</c:if>

</div>

<saos:pagePagination pageLink="${pageLink}" pageNo="${pageNo}" totalPages="${totalPages}" ></saos:pagePagination>
