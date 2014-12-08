<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<saos:formFieldText path="scJudgmentForm" labelName="input-search-judgment-form" labelText="judgmentSearch.formField.scJudgmentForm" />

<div class="form-group">
	<label for="" class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.personnelType" />:</label>
   	<div class="col-sm-7">
    	<form:select path="scPersonnelType" class="form-control" id="input-search-personnel-type" >
    		<option></option>
    		<saos:enumOptions enumType="pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType" selected="${judgmentCriteriaForm.scPersonnelType}" dataTag="true"/>
    	</form:select>
	</div>
</div>

<saosSearch:courtSelect items="${supremeChambers}" selectedItem="${judgmentCriteriaForm.supremeChamberId}" path="supremeChamberId" id="select-supreme-chamber" labelName="select-supreme-chamber" labelText="judgmentSearch.formField.supremeChamber" />

<saosSearch:courtSelect items="${supremeChamberDivisions}" selectedItem="${judgmentCriteriaForm.supremeChamberDivisionId}" path="supremeChamberDivisionId" id="select-supreme-chamber-division" labelName="select-supreme-chamber-division" labelText="judgmentSearch.formField.supremeChamberDivision" />

