<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<form:form id="search-form" class="form-horizontal" role="form" modelAttribute="judgmentCriteriaForm" action="${contextPath}/search" method="GET">

<div class="container search-form block">

	<h3><spring:message code="search.form.header" /></h3>
	
	<fieldset id="search-fieldset" >
	
		<saos:formFieldText path="all" labelName="input-search-all" labelText="search.field.all" />
    
	    <ul>
	    	<li>
	    		<a id="search-more-fields" href="#" ><spring:message code="search.form.morefields" /></a>
    		</li>
	    </ul>
	    
	    <div id="advance-form">
	    
   			<saos:formFieldText path="signature" labelName="input-search-casenumber" labelText="search.field.casenumber" />

		    <div class="form-group">
			    <label for="datepicker_from" class="col-sm-2 control-label"><spring:message code="search.field.date" />:</label>
			    <div class="col-sm-2">
  		 			<spring:message code="search.field.date.from" var="labelDateFrom" />
			    	<form:input path="dateFrom" class="form-control" id="datepicker_from" placeholder="${labelDateFrom}" />
			       
			    </div>
			    <label for="datepicker_to" class="col-sm-1 control-label"></label>
			    <div class="col-sm-2">
			    	<spring:message code="search.field.date.from" var="labelDateTo" />
			    	<form:input path="dateTo" class="form-control" id="datepicker_to" placeholder="${labelDateTo}" />
			    </div>
		    </div>
		    
		    <div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="search.field.judgmenttype" />:</label>
			    <div class="col-sm-6">
				  <div class="checkbox">
			   	        <saos:enumCheckBox path="judgmentType" enumType="pl.edu.icm.saos.persistence.model.Judgment.JudgmentType" prefix="judgment.judgmenttype" />
			   </div>
			 </div>
			</div>
		    
		    <saos:formFieldText path="judgeName" labelName="input-search-judge" labelText="search.field.judge" />
		    		    
			<saos:formFieldText path="legalBase" labelName="input-search-legalbases" labelText="search.field.legalbases" />
			 
			<saos:formFieldText path="referencedRegulation" labelName="input-search-referencedregulations" labelText="search.field.referencedregulations" />
		    
		    <div class="form-group">
			    <label class="col-sm-2 control-label"><spring:message code="search.field.courttype" />:</label>
			    <div class="col-sm-7">
			    	<form:radiobutton path="courtType" id="radio-all" value="all" checked="true" />
			    	<label for="radio-all" ><spring:message code="judgment.courttype.all" /></label>
			    	<saos:enumRadio path="courtType" enumType="pl.edu.icm.saos.webapp.judgment.CourtType" id="court" prefix="judgment.courttype" />
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
	    		<a id="search-less-fields" href="#" ><spring:message code="search.form.lessfields" /></a>
    		</li>
	    </ul>
	
		<div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		      <button type="submit" class="btn btn-primary button button-blue"><spring:message code="search.button.search" /></button>
		      <button type="reset" class="btn btn-primary button button-blue"><spring:message code="search.button.reset" /></button>
		    </div>
   		</div>
	
	</fieldset>
	
</div>

<div class="container judgment-filter block " >

	<c:set var="lastItem" value="${(pageNo+1)*(pageSize)}" />

	<c:if test="${pageNo == totalPages || totalPages == 0}" >
		<c:set var="lastItem" value="${resultsNo}" />
	</c:if>

	<h3><spring:message code="judgment.results.header" /><span><spring:message code="judgment.results.info" arguments="${resultsNo}" /></span></h3>
	
	<div id="filter-box-button" class="filter-box-button display-none" data-toggle="tooltip" data-placement="top" title="<spring:message code='judgment.results.filter.show' />"></div>
	
	<div class="search-settings" id="search-settings" data-toggle="tooltip" data-placement="top" title="<spring:message code='search.tooltip.settings' />" >
		
	</div>
	
	
	<div class="settings-box" id="settings-box" >
		<div class="" >
			<div class="label"><spring:message code="judgment.results.sort.pageSize" />:</div>
			<select id="searchPageSize" name="size">
				<option value="10" <c:if test="${pageSize==10}"> selected="selected"</c:if>>10</option>
			    <option value="20" <c:if test="${pageSize==20}"> selected="selected"</c:if>>20</option>
			    <option value="50" <c:if test="${pageSize==50}"> selected="selected"</c:if>>50</option>
			    <option value="100" <c:if test="${pageSize==100}"> selected="selected"</c:if>>100</option>
			</select>
		</div>
		<div>
			<div class="label" ><spring:message code="judgment.results.sort.by" />:</div>
			<select id="searchSorting" name="sort">
				<option value="RELEVANCE" <c:if test="${sortProperty=='RELEVANCE'}"> selected="selected"</c:if> ><spring:message code="judgment.results.sort.accuracy" /></option>
				<option value="JUDGMENT_DATE" <c:if test="${sortProperty == 'JUDGMENT_DATE'}"> selected="selected"</c:if> ><spring:message code="judgment.results.sort.date" /></option>
			    <option value="COURT_ID" <c:if test="${sortProperty == 'COURT_ID'}"> selected="selected" </c:if> ><spring:message code="judgment.results.sort.court" /></option>
			</select>
			
			<label><spring:message code="judgment.results.sort.direction" />:</label>
			<input id="searchSortingDirection" type="checkbox" value="checked" <c:if test="${sortDirection == 'ASC'}"> checked="checked" </c:if> />
		</div>
	</div>		
		
</div>

</form:form>

