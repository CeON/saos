<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<form:form id="search-form" class="form-horizontal" role="form" modelAttribute="judgmentCriteriaForm" action="${contextPath}/search" method="GET">

<div class="container search-form block">

	<h3><spring:message code="judgmentSearch.form.header" /></h3>
	
	<fieldset id="search-fieldset" >
	
		<saos:formFieldText path="all" labelName="input-search-all" labelText="judgmentSearch.formField.all" />
    
	    <ul>
	    	<li>
	    		<a id="search-more-fields" href="#" ><spring:message code="judgmentSearch.form.moreFields" /></a>
    		</li>
	    </ul>
	    
	    <div id="advance-form">
	    
   			<saos:formFieldText path="signature" labelName="input-search-casenumber" labelText="judgmentSearch.formField.caseNumber" />

		    <div class="form-group">
			    <label for="datepicker_from" class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.date" />:</label>
			    <div class="col-sm-2">
  		 			<spring:message code="judgmentSearch.formField.dateFrom" var="labelDateFrom" />
			    	<form:input path="dateFrom" class="form-control" id="datepicker_from" placeholder="${labelDateFrom}" />
			       
			    </div>
			    <label for="datepicker_to" class="col-sm-1 control-label"></label>
			    <div class="col-sm-2">
			    	<spring:message code="judgmentSearch.formField.dateFrom" var="labelDateTo" />
			    	<form:input path="dateTo" class="form-control" id="datepicker_to" placeholder="${labelDateTo}" />
			    </div>
		    </div>
		    
		    <div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.judgmentType" />:</label>
			    <div class="col-sm-6">
				  <div class="checkbox">
			   	        <saos:enumCheckboxes path="judgmentType" enumType="pl.edu.icm.saos.persistence.model.Judgment.JudgmentType" />
			   </div>
			 </div>
			</div>
		    
		    <saos:formFieldText path="judgeName" labelName="input-search-judge" labelText="judgmentSearch.formField.judge" />
		    		    
			<saos:formFieldText path="legalBase" labelName="input-search-legalbases" labelText="judgmentSearch.formField.legalBases" />
			 
			<saos:formFieldText path="referencedRegulation" labelName="input-search-referencedregulations" labelText="judgmentSearch.formField.referencedRegulations" />
		    
		    <div class="form-group">
			    <label class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.courtType" />:</label>
			    <div class="col-sm-7">
			    	<form:radiobutton path="courtType" id="radio-all" value="" checked="true" />
			    	<label for="radio-all" ><spring:message code="judgment.courtType.all" /></label>
			    	<saos:enumRadios path="courtType" enumType="pl.edu.icm.saos.persistence.model.CourtType" id="court" />
			    </div>
		    </div>
		    
		    <div id="all-fields" class="fields-container" >
		    	
		    </div>
		    
		    <div id="common-court-fields" class="fields-container" >
			    <%@ include file="ccJudgment/ccJudgmentSearchForm.jsp" %>
		    </div>
		     
		    <div id="supreme-court-fields" class="fields-container" >
		    	<%@ include file="scJudgment/scJudgmentSearchForm.jsp" %>
		    </div>
		     
	    </div>
	
		<ul class="display-none" >
	    	<li>
	    		<a id="search-less-fields" href="#" ><spring:message code="judgmentSearch.form.lessFields" /></a>
    		</li>
	    </ul>
	
		<div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		      <button type="submit" class="btn btn-primary button button-blue"><spring:message code="button.search" /></button>
		      <button type="reset" class="btn btn-primary button button-blue"><spring:message code="button.reset" /></button>
		    </div>
   		</div>
	
	</fieldset>
	
</div>

<div class="container judgment-filter block " >

	<c:set var="lastItem" value="${(pageNo+1)*(pageSize)}" />

	<c:if test="${pageNo == totalPages || totalPages == 0}" >
		<c:set var="lastItem" value="${resultsNo}" />
	</c:if>

	<h3><spring:message code="judgmentSearch.results.header" /><span><spring:message code="judgmentSearch.results.judgmentsNumber" arguments="${resultsNo}" /></span></h3>
	
	<div id="filter-box-button" class="filter-box-button display-none" data-toggle="tooltip" data-placement="top" title="<spring:message code='judgmentSearch.filterBox.show' />"></div>
	
	<div class="search-settings" id="search-settings" data-toggle="tooltip" data-placement="top" title="<spring:message code='judgmentSearch.tooltip.settings' />" >
		
	</div>
	
	
	<div class="settings-box" id="settings-box" >
		<div class="" >
			<div class="label"><spring:message code="sort.pageSize" />:</div>
			<select id="searchPageSize" name="size">
				<option value="10" <c:if test="${pageSize==10}"> selected="selected"</c:if>>10</option>
			    <option value="20" <c:if test="${pageSize==20}"> selected="selected"</c:if>>20</option>
			    <option value="50" <c:if test="${pageSize==50}"> selected="selected"</c:if>>50</option>
			    <option value="100" <c:if test="${pageSize==100}"> selected="selected"</c:if>>100</option>
			</select>
		</div>
		<div>
			<c:set var="sortDirectionValue" value=",${fn:toLowerCase(sortDirection)}"/>
		
			<div class="label" ><spring:message code="sort.by" />:</div>
			<select id="searchSorting" name="sort">
				<option value="RELEVANCE${sortDirectionValue}" <c:if test="${sortProperty=='RELEVANCE'}"> selected="selected"</c:if> ><spring:message code="sort.accuracy" /></option>
				<option value="JUDGMENT_DATE${sortDirectionValue}" <c:if test="${sortProperty == 'JUDGMENT_DATE'}"> selected="selected"</c:if> ><spring:message code="sort.date" /></option>
			    <option value="COURT_ID${sortDirectionValue}" <c:if test="${sortProperty == 'COURT_ID'}"> selected="selected" </c:if> ><spring:message code="sort.court" /></option>
			</select>
			
			<label><spring:message code="sort.direction" />:</label>
			<input id="searchSortingDirection" type="checkbox" value="checked" <c:if test="${sortDirection == 'ASC'}"> checked="checked" </c:if> />
		</div>
	</div>		
		
</div>

</form:form>

