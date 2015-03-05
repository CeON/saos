<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<form:form id="search-form" class="form-horizontal" role="form" modelAttribute="judgmentCriteriaForm" action="${contextPath}/search" method="GET">

<div class="container search-form block">


	<fieldset id="search-fieldset" >
		<legend><spring:message code="judgmentSearch.form.header" /></legend>
		<saos:formFieldText path="all" labelName="input-search-all" labelText="judgmentSearch.formField.all" />
    
	    <div class="col-sm-offset-2 " >
    		<a id="search-more-fields" class="button-advance" href="#search-form" ><spring:message code="judgmentSearch.form.moreFields" /></a>
	    </div>
	    
	    <div id="advance-form" class="advance-form" >
	    
   			<saos:formFieldText path="signature" labelName="input-search-casenumber" labelText="judgmentSearch.formField.caseNumber" />

		    <div class="form-group">
		    	<spring:message code="judgmentSearch.formField.datePlaceholder" var="datePlaceholder" />	
			    <label for="datepicker_from" class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.dateFrom" />:</label>
			    <div class="col-sm-7">
			    	<form:input path="dateFrom" class="form-control form-date" id="datepicker_from" placeholder="${datePlaceholder}" maxlength="10" />
			    	<label id="datepicker_from-desc" class="" for="datepicker_from"><spring:message code="judgmentSearch.formField.dateWrongFormat" /></label>			       
			    </div>
		    </div>
		    <div class="form-group">
			    <label for="datepicker_to" class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.dateTo" />:</label>
			    <div class="col-sm-7">
			    	<form:input path="dateTo" class="form-control form-date" id="datepicker_to" placeholder="${datePlaceholder}" maxlength="10" />
			    	<label id="datepicker_to-desc" class="" for="datepicker_to"><spring:message code="judgmentSearch.formField.dateWrongFormat" /></label>
			    </div>
		    </div>
		    
		    <div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.judgmentType" />:</label>
				<div class="col-sm-7">
					<div class="checkbox">

			   			<spring:eval expression="T(pl.edu.icm.saos.persistence.model.Judgment.JudgmentType).values()" var="enumJudgmentTypes" scope="page"/>
			   		
			   			<c:forEach var="enumValue" items="${enumJudgmentTypes}">
							<c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
							<div class="col-xs-6">
								<form:checkbox path="judgmentTypes" value="${enumValue}" id="checkbox-${lowerCaseEnumValue}" ></form:checkbox>
								<label for="checkbox-${lowerCaseEnumValue}" >
									<saos:enum value="${enumValue}" />
								</label>
								
								<c:if test="${enumValue == 'REASONS'}" >
									<spring:eval expression="@exposedProperties.getProperty('judgmentSource.COMMON_COURT.url')" var="ccJudgmentSourceUrl" />
									<c:set var="ccJudgmentSourceLink"  >
										<a href='http://${ccJudgmentSourceUrl}' >${ccJudgmentSourceUrl}</a> 
									</c:set>
									
									<spring:message code="judgmentSearch.hint.reasons.title" var="hintReasonsTitle" />
									<spring:message code="judgmentSearch.hint.reasons.content" var="hintReasonsContent" arguments="${ccJudgmentSourceLink}" />	
									<saos:hint title="${hintReasonsTitle}" content="${hintReasonsContent}" />
								</c:if>
							</div>
						</c:forEach>
		   		 	
			   		</div>
			 	</div>
			</div>
		    
		    
		    <saos:formFieldText path="judgeName" labelName="input-search-judge" labelText="judgmentSearch.formField.judge" />

			<%-- Legal bases --%>
			<div class="form-group">
				<label for="input-search-legalbases" class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.legalBases" />:</label>
			   	<div class="col-sm-7">
			    	<form:input path="legalBase" class="form-control" id="input-search-legalbases" />
				</div>
				<spring:message code="judgmentSearch.hint.legalBases.title" var="hintLegalBasesTitle" />
				<spring:message code="judgmentSearch.hint.legalBases.content" var="hintLegalBasesContent" />
				<saos:hint title="${hintLegalBasesTitle}" content="${hintLegalBasesContent}" />
			</div>
			
		    
		    <%-- Law journal entry --%>
		    <div class="form-group law-journal">
		    	<label for="input-search-referencedregulations" class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.referencedRegulations" /></label>
			   	<div class="col-sm-7">
			   		 
			   		 <div id="law-journal-navigation" >
			   		 	
			   		 	<c:if test="${!empty lawJournalEntry}" >
			   		 		<div class="selected-law">
			   		 			<span>
				   		 			<c:out value="${lawJournalEntry.year}" />/<c:out value="${lawJournalEntry.journalNo}" />/<c:out value="${lawJournalEntry.entry}" />
				   		 			 -
				   		 			<c:out value="${lawJournalEntry.title}" /> 
			   		 			</span>
			   		 			<a href="" class="remove"></a>
		   		 			</div>
			   		 	</c:if>
			   		 	 
			   			<button type="button" id="law-journal-set" class="btn btn-primary button-blue btn-sm"><spring:message code="judgmentSearch.formField.lawJournal.set" /></button> 
			   		 </div>
			   		
			   		<div id="law-journal-fields" class="well col-sm-12" >
			   			
			   			<spring:message code="judgmentSearch.formField.lawJournal.close" var="lawJournalClose" />
		   				<a class="remove law-close" href="" id="law-journal-close" title="${lawJournalClose}" data-placement="top" data-toggle="tooltip"  ></a>
			   			
			   			<div class="row">
			   			
			   				<div class="col-md-6">
				   				<spring:message code="judgmentSearch.formField.lawJournal.year" />/
				   				<spring:message code="judgmentSearch.formField.lawJournal.journalNo" />/
				   				<spring:message code="judgmentSearch.formField.lawJournal.entry" />
				   			</div>
				   			
				   			<div class="col-md-6">
				   				<spring:message code="judgmentSearch.formField.lawJournal.text" />:
				   			</div>
			   			</div>
			   		
			   			<div class="row">
			   							   			
				   			<div class="col-md-6">
				   				<input class="form-control" id="law-journal-year" type="text" size="4" />/
				   				<input class="form-control" id="law-journal-journalNo" type="text" size="4" />/
				   				<input class="form-control" id="law-journal-entry" type="text" size="4" />
				   			</div>
				   			
				   			<div class="col-md-6">
				   				<input class="form-control width-full" id="law-journal-text" type="text" />
				   			</div>
			   			</div>
				    	
				    	<div class="col-sm-12">
				    		<ul id="law-journal-list" ></ul>
				    		<a href="" id="law-journal-more" class="law-journal-more display-none" ><spring:message code="more" /></a>
				    	</div>
				    	
		    		</div>
			    	
				</div>
		    	<form:hidden path="lawJournalEntryId" id="lawJournalEntryId" />
		    </div>
		    
		    
		    <div class="form-group radio-group">
			    <label class="col-sm-2 control-label"><spring:message code="judgmentSearch.formField.courtType" />:</label>
			    <div class="col-sm-7">
			    	<div class="col-sm-6">
				    	<form:radiobutton path="courtType" id="radio-all" value="" checked="checked" />
				    	<label for="radio-all" ><spring:message code="judgmentSearch.formField.courtTypeAny" /></label>
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
		    
		    <div id="constitutional-tribunal-fields" class="fields-container">
		    	<%@ include file="ctJudgment/ctJudgmentSearchForm.jsp" %>
		    </div>
		     
	    </div>
	
		<div class="col-sm-offset-2" >
			<a id="search-less-fields" class="button-advance visibility-hidden display-none" href="#search-form" ><spring:message code="judgmentSearch.form.lessFields" /></a>
		</div>
	
		<div class="form-group button-group">
			<div class="col-sm-9">
				<button type="reset" class="btn btn-default button"><spring:message code="button.resetForm" /></button>
				<button type="submit" class="btn btn-primary button button-blue"><spring:message code="button.judgmentSearch" /></button>
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
	
	<a id="filter-box-button" class="filter-box-button button-settings display-none" href="" data-toggle="tooltip" data-placement="top" title="<spring:message code='judgmentSearch.filterBox.show' />">
		<img src="${contextPath}/static/image/icons/filter.png" height="20" alt="<spring:message code="judgmentSearch.filterBox.iconAlt" />" />
	</a>
	
	<a id="search-settings"  class="search-settings button-settings" href="" data-toggle="tooltip" data-placement="top" title="<spring:message code="judgmentSearch.tooltip.settings" />" >
		<img src="${contextPath}/static/image/icons/settings.png" height="20" alt="<spring:message code="judgmentSearch.settings.iconAlt" />" />
	</a>
	
	
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
				</select>
				
				<label for="searchSortingDirection" ><spring:message code="sort.direction" />:</label>
				<input id="searchSortingDirection" type="checkbox" value="checked" <c:if test="${sortDirection == 'ASC'}"> checked="checked" </c:if> />
			</div>
		</fieldset>
	</div>		
		
</div>

</form:form>

