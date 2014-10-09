<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<saosSearch:courtSelect items="${courts}" selectedItem="${judgmentCriteriaForm.courtId }" path="courtId" id="select-court" labelName="input-search-court" labelText="search.field.court" />

<saosSearch:courtSelect items="${divisions}" selectedItem="${judgmentCriteriaForm.divisionId }" path="divisionId" id="select-division" labelName="input-search-division" labelText="search.field.division" />

<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="search.field.judgmenttype" />:</label>
    <div class="col-sm-6">
	  <div class="checkbox">
   	        <saos:enumCheckBox path="judgmentType" enumType="pl.edu.icm.saos.persistence.model.Judgment.JudgmentType" prefix="judgment.judgmenttype" />
   </div>
 </div>
</div>


<saos:formFieldText path="legalBase" labelName="input-search-legalbases" labelText="search.field.legalbases" />
 
<saos:formFieldText path="referencedRegulation" labelName="input-search-referencedregulations" labelText="search.field.referencedregulations" />

