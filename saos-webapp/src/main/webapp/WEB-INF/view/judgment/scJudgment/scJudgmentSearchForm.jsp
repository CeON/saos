<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<saos:formFieldText path="scJudgmentForm" labelName="input-search-judgment-form" labelText="search.field.scJudgmentForm" />

<div class="form-group">
	<label for="" class="col-sm-2 control-label"><spring:message code="search.field.personneltype" />:</label>
   	<div class="col-sm-7">
    	<select class="form-control" id="personnel-type" >
    		<option></option>
    		<saos:enumOptions enumType="pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType" />
    	</select>
	</div>
</div>


<saosSearch:courtSelect items="${supremeChambers}" selectedItem="${judgmentCriteriaForm.supremeChamberId}" path="supremeChamberId" id="select-supreme-chamber" labelName="select-supreme-chamber" labelText="search.field.supreme.chamber" />

<saosSearch:courtSelect items="${supremeChamberDivisions}" selectedItem="${judgmentCriteriaForm.supremeChamberDivisionId}" path="supremeChamberDivisionId" id="select-supreme-chamber-division" labelName="select-supreme-chamber-division" labelText="search.field.supreme.chamberdivision" />

