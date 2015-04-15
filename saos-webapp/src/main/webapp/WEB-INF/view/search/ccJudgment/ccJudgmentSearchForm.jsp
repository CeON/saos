<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<saosSearch:courtSelect items="${commonCourts}" selectedItem="${judgmentCriteriaForm.commonCourtId}" path="commonCourtId" id="select-common-court" labelName="select-common-court" labelText="judgmentSearch.formField.commonCourt" labelEmptyOption="judgmentSearch.formField.chooseCommonCourt" />

<saosSearch:courtSelect items="${commonCourtDivisions}" selectedItem="${judgmentCriteriaForm.commonCourtDivisionId}" path="commonCourtDivisionId" id="select-common-division" labelName="select-common-division" labelText="judgmentSearch.formField.commonDivision"  labelEmptyOption="judgmentSearch.formField.chooseCcDivision" />

<div class="form-group">
    <label for="input-search-keywords-cc" class="col-lg-2 col-sm-3 col-xs-12 control-label"><spring:message code="judgmentSearch.formField.keywords" />:</label>
    <div class="col-lg-7 col-sm-8 col-xs-11">
        <form:input path="keywords" class="form-control" id="input-search-keywords-cc" />
    </div>
    <spring:message code="judgmentSearch.hint.ccKeywords.title" var="hintCcKeywordsTitle" />
    <spring:message code="judgmentSearch.hint.ccKeywords.content" var="hintCcKeywordsContent" />
    <div >
        <saos:hint title="${hintCcKeywordsTitle}" content="${hintCcKeywordsContent}" placement="left" />
    </div>
</div>