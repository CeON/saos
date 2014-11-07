<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<saos:filterField assignedField="select-supreme-chamber" label="judgment.results.filter.chamber" filterValue="${judgmentCriteriaForm.supremeChamberId}" id="filter-supreme-chamber"></saos:filterField>

<saos:filterField assignedField="select-supreme-chamber-division" label="judgment.results.filter.chamberdivision" filterValue="${judgmentCriteriaForm.supremeChamberDivisionId}" id="filter-supreme-chamber-division"></saos:filterField>
		