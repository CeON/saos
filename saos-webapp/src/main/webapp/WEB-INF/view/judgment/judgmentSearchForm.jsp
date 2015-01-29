<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<form:form id="search-form" class="form-horizontal" role="form" modelAttribute="judgmentCriteriaForm" action="${contextPath}/search" method="GET">

<div class="container search-form block">


	<fieldset id="search-fieldset" >
		<legend><spring:message code="judgmentSearch.form.header" /></legend>
		<saos:formFieldText path="all" labelName="input-search-all" labelText="judgmentSearch.formField.all" />
    
	    <ul>
	    	<li>
	    		<a id="search-more-fields" href="#" ><spring:message code="judgmentSearch.form.moreFields" /></a>
    		</li>
	    </ul>
	    
	    <div id="advance-form" class="advance-form" >
	    
   			<saos:formFieldText path="signature" labelName="input-search-casenumber" labelText="judgmentSearch.formField.caseNumber" />

		    <div class="form-group">
		    	<spring:message code="judgmentSearch.formField.datePlaceholder" var="datePlaceholder" />	
			    <label for="datepicker_from" class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.dateFrom" />:</label>
			    <div class="col-sm-7">
			    	<form:input path="dateFrom" class="form-control form-date" id="datepicker_from" placeholder="${datePlaceholder}" maxlength="10" />			       
			    </div>
		    </div>
		    <div class="form-group">
			    <label for="datepicker_to" class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.dateTo" />:</label>
			    <div class="col-sm-7">
			    	<form:input path="dateTo" class="form-control form-date" id="datepicker_to" placeholder="${datePlaceholder}" maxlength="10" />
			    </div>
		    </div>
		    
		    <div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.judgmentType" />:</label>
				<div class="col-sm-7">
					<div class="checkbox">
		   	    	    <saos:enumCheckboxes path="judgmentTypes" enumType="pl.edu.icm.saos.persistence.model.Judgment.JudgmentType" columnsNumber="2" />
			   		</div>
			 	</div>
			</div>
		    
		    <saos:formFieldText path="judgeName" labelName="input-search-judge" labelText="judgmentSearch.formField.judge" />
		    		    
			<saos:formFieldText path="legalBase" labelName="input-search-legalbases" labelText="judgmentSearch.formField.legalBases" />
			 
			<saos:formFieldText path="referencedRegulation" labelName="input-search-referencedregulations" labelText="judgmentSearch.formField.referencedRegulations" />
		    
		    <div class="form-group">
			    <label class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.courtType" />:</label>
			    <div class="col-sm-7">
			    	<div class="col-xs-6">
				    	<form:radiobutton path="courtType" id="radio-all" value="" checked="checked" />
				    	<label for="radio-all" ><spring:message code="judgment.courtType.all" /></label>
			    	</div>
			    	<saos:enumRadios path="courtType" enumType="pl.edu.icm.saos.persistence.model.CourtType" id="court" columnsNumber="2" />
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
	
		<ul class="visibility-hidden display-none" >
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

	<h2><spring:message code="judgmentSearch.results.header" /><span><spring:message code="judgmentSearch.results.judgmentsNumber" arguments="${resultsNo}" /></span></h2>
	
	<a id="filter-box-button" class="filter-box-button display-none" data-toggle="tooltip" data-placement="top" title="<spring:message code='judgmentSearch.filterBox.show' />"></a>
	
	<a class="search-settings" id="search-settings" href="#" data-toggle="tooltip" data-placement="top" title="<spring:message code='judgmentSearch.tooltip.settings' />" ></a>
	
	
	<div class="settings-box" id="settings-box" >
		<fieldset>
			<legend><spring:message code="sort.title" /></legend>
			<div class="" >
				<label class="label" for="searchPageSize" ><spring:message code="sort.pageSize" />:</label>
				<select id="searchPageSize" name="size">
					<option value="10" <c:if test="${pageSize==10}"> selected="selected"</c:if>>10</option>
				    <option value="20" <c:if test="${pageSize==20}"> selected="selected"</c:if>>20</option>
				    <option value="50" <c:if test="${pageSize==50}"> selected="selected"</c:if>>50</option>
				    <option value="100" <c:if test="${pageSize==100}"> selected="selected"</c:if>>100</option>
				</select>
			</div>
			<div>
				<c:set var="sortDirectionValue" value=",${fn:toLowerCase(sortDirection)}"/>
			
				<label class="label" for="searchSorting" ><spring:message code="sort.by" />:</label>
				<select id="searchSorting" name="sort">
					<option value="RELEVANCE${sortDirectionValue}" <c:if test="${sortProperty=='RELEVANCE'}"> selected="selected"</c:if> ><spring:message code="sort.accuracy" /></option>
					<option value="JUDGMENT_DATE${sortDirectionValue}" <c:if test="${sortProperty == 'JUDGMENT_DATE'}"> selected="selected"</c:if> ><spring:message code="sort.date" /></option>
				    <option value="COURT_ID${sortDirectionValue}" <c:if test="${sortProperty == 'COURT_ID'}"> selected="selected" </c:if> ><spring:message code="sort.court" /></option>
				</select>
				
				<label for="searchSortingDirection" ><spring:message code="sort.direction" />:</label>
				<input id="searchSortingDirection" type="checkbox" value="checked" <c:if test="${sortDirection == 'ASC'}"> checked="checked" </c:if> />
			</div>
		</fieldset>
	</div>		
		
</div>

</form:form>

