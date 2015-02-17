<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<saosSearch:courtSelect items="${commonCourts}" selectedItem="${judgmentCriteriaForm.commonCourtId}" path="commonCourtId" id="select-common-court" labelName="select-common-court" labelText="judgmentSearch.formField.commonCourt" labelEmptyOption="judgmentSearch.formField.chooseCommonCourt" />

<saosSearch:courtSelect items="${commonCourtDivisions}" selectedItem="${judgmentCriteriaForm.commonCourtDivisionId}" path="commonCourtDivisionId" id="select-common-division" labelName="select-common-division" labelText="judgmentSearch.formField.commonDivision"  labelEmptyOption="judgmentSearch.formField.chooseCcDivision" />

<saos:formFieldText path="keywords" labelName="input-search-keywords-cc" labelText="judgmentSearch.formField.keywords" />



