<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<saos:filterField assignedField="select-common-court" label="judgment.results.filter.court" filterValue="${judgmentCriteriaForm.commonCourtId}" id="filter-court"></saos:filterField>
	
<saos:filterField assignedField="select-common-division" label="judgment.results.filter.division" filterValue="${judgmentCriteriaForm.commonCourtDivisionId}" id="filter-division"></saos:filterField>

<saos:filterField assignedField="input-search-keywords" label="judgment.results.filter.keywords" filterValue="${judgmentCriteriaForm.keywords}" id="filter-keyword"></saos:filterField>
 	
	
		