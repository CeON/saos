<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<%-- JugmentForm --%>
<div class="form-group">
	<label for="select-search-judgment-form" class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.scJudgmentForm" />:</label>
   	<div class="col-sm-7">
		<form:select path="scJudgmentForm" id="select-search-judgment-form" class="form-control" >
		 	<option value=""><spring:message code="judgmentSearch.formField.chooseScJudgmentForm" /></option>
			<c:forEach items="${scJudgmentForms}" var="judgmentForm" >
				<option value="${judgmentForm.name}" <c:if test="${judgmentCriteriaForm.scJudgmentForm == judgmentForm.name}" >selected="selected"</c:if> >
					<c:out value="${judgmentForm.name}" />
				</option>
			</c:forEach>
		</form:select>
	</div>
</div>

<%-- PersonnelType --%>
<div class="form-group">
	<label for="select-search-personnel-type" class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.personnelType" />:</label>
   	<div class="col-sm-7">
    	<form:select path="scPersonnelType" class="form-control" id="select-search-personnel-type" >
    		<option value=""><spring:message code="judgmentSearch.formField.chooseScPersonnelType" /></option>
    		<saos:enumOptions enumType="pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType" selected="${judgmentCriteriaForm.scPersonnelType}" />
    	</form:select>
	</div>
</div>

<%-- SupremeCourtChamber --%>
<saosSearch:courtSelect items="${supremeChambers}" selectedItem="${judgmentCriteriaForm.supremeChamberId}" path="supremeChamberId" id="select-supreme-chamber" labelName="select-supreme-chamber" labelText="judgmentSearch.formField.supremeChamber" labelEmptyOption="judgmentSearch.formField.chooseSupremeChamber" />

<%-- SupremeCourtChamberDivision --%>
<saosSearch:courtSelect items="${supremeChamberDivisions}" selectedItem="${judgmentCriteriaForm.supremeChamberDivisionId}" path="supremeChamberDivisionId" id="select-supreme-chamber-division" labelName="select-supreme-chamber-division" labelText="judgmentSearch.formField.supremeChamberDivision" labelEmptyOption="judgmentSearch.formField.chooseScDivision" />

