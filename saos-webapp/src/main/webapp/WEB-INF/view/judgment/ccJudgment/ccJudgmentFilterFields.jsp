<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<saos:filterField assignedField="select-common-court" label="judgmentSearch.formField.commonCourt" filterValue="${judgmentCriteriaForm.commonCourtId}" id="filter-court"></saos:filterField>
	
<saos:filterField assignedField="select-common-division" label="judgmentSearch.formField.commonDivision" filterValue="${judgmentCriteriaForm.commonCourtDivisionId}" id="filter-division"></saos:filterField>

<saos:filterField assignedField="input-search-keywords" label="judgmentSearch.formField.keywords" filterValue="${judgmentCriteriaForm.keywords}" id="filter-keyword"></saos:filterField>
 	

		