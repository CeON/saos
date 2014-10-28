<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<saosSearch:courtSelect items="${commonCourts}" selectedItem="${judgmentCriteriaForm.commonCourtId}" path="commonCourtId" id="select-common-court" labelName="select-common-court" labelText="search.field.court" />

<saosSearch:courtSelect items="${commonCourtDivisions}" selectedItem="${judgmentCriteriaForm.commonCourtDivisionId}" path="commonCourtDivisionId" id="select-common-division" labelName="select-common-division" labelText="search.field.division" />

<saos:formFieldText path="keyword" labelName="input-search-keywords" labelText="search.field.keywords" />