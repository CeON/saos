<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<saosSearch:courtSelect items="${courts}" selectedItem="${judgmentCriteriaForm.courtId }" path="courtId" id="select-court" labelName="input-search-court" labelText="search.field.court" />

<saosSearch:courtSelect items="${divisions}" selectedItem="${judgmentCriteriaForm.divisionId }" path="divisionId" id="select-division" labelName="input-search-division" labelText="search.field.division" />

<saos:formFieldText path="keyword" labelName="input-search-keywords" labelText="search.field.keywords" />