<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<saos:filterField assignedField="select-search-judgment-form" label="judgmentSearch.formField.scJudgmentForm" filterValue="${judgmentCriteriaForm.scJudgmentForm}" id="filter-supreme-judgment-form"></saos:filterField>

<saos:filterField assignedField="select-search-personnel-type" label="judgment.personnelType" filterValue="${judgmentCriteriaForm.scPersonnelType}" id="filter-supreme-personnel-type"></saos:filterField>

<saos:filterField assignedField="select-supreme-chamber" label="judgmentSearch.formField.supremeChamber" filterValue="${judgmentCriteriaForm.courtCriteria.scCourtChamberId}" id="filter-supreme-chamber"></saos:filterField>

<saos:filterField assignedField="select-supreme-chamber-division" label="judgmentSearch.formField.supremeChamberDivision" filterValue="${judgmentCriteriaForm.courtCriteria.scCourtChamberDivisionId}" id="filter-supreme-chamber-division"></saos:filterField>
		