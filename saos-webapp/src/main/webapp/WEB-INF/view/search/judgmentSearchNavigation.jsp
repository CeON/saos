<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>



<a href="${contextPath}/" class="saos-logo" title="" ></a>

<div class="search-form" id="search-form-section" tabindex=0>
    <fieldset id="search-fieldset" >
    
        <legend><spring:message code="judgmentSearch.form.header" /></legend>   
        
        <%-- Search form hint --%>
        <div class="search-form-hint">
          <spring:message var="searchQueryLanguage" code="judgmentSearch.hint.searchQueryLanguage" />
          <spring:eval expression="@exposedProperties.getProperty('webapp.helpAddress.searchQueryLanguage')" var="linkToHelpAboutSearchQueryLanguage" />
          
          <c:set var="searchFormHintAddress" >
              <saos:helpLink title="${searchQueryLanguage}" pageLink="${linkToHelpAboutSearchQueryLanguage}" ></saos:helpLink>
          </c:set>
          
          <spring:message var="searchFormHintTitle" code="judgmentSearch.hint.searchForm.title" />
          <spring:message var="searchFormHintContent" code="judgmentSearch.hint.searchForm.content" arguments="${searchFormHintAddress}" />
    
          <saos:hint content="${searchFormHintContent}" title="${searchFormHintTitle}" placement="left" ></saos:hint>
        </div>
    
    
		<c:if test="${referencedJudgment != null}">
		    <%@ include file="judgmentSearchContext.jsp" %>       
	    </c:if>
    
    
        <%-- Global search criteria --%>        
        <%@ include file="judgmentSearchGlobalContext.jsp" %>
        
               
    
        <div class="form-section-container" >
        
            <div class="info-section-header" >
                <spring:message code="judgmentSearch.judgmentSection.criteria.header" />
           </div>
         
           <a id="judgment-info" class="info-section" href="" >
               <spring:message code="judgmentSearch.judgmentSection.defaultText" />
           </a>
               
    
            
            <div id="judgment-form-section" class="row form-section">
                
    
                    <%-- Case number --%>
                    <spring:message code="judgmentSearch.formField.caseNumber" var="caseNumberLabel" />
                    <div class="form-group" >
                        <label for="input-search-casenumber" class="col-xs-12 field-label">
                            <c:out value="${caseNumberLabel}" />:
                        </label>
                        <div class="col-xs-11">
                            <form:input path="signature" class="form-control" id="input-search-casenumber" data-field-desc="${caseNumberLabel}: " />
                        </div>
                    </div>
                                        
                    <%-- JudgmentType --%>
                    <div class="form-group">
                        <label class="col-xs-12 field-label"><spring:message code="judgmentSearch.formField.judgmentType" />:</label>
                        <div class="col-xs-12">
                            <div class="checkbox">
        
                                <spring:eval expression="T(pl.edu.icm.saos.persistence.model.Judgment.JudgmentType).values()" var="enumJudgmentTypes" scope="page"/>
                            
                                <c:forEach var="enumValue" items="${enumJudgmentTypes}">
                                    <c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
                                    <div class="col-xs-12">
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
                    
                    
                    <%-- Supreme JugmentForm --%>
                    <spring:message code="judgmentSearch.formField.scJudgmentForm" var="scJudgmentFormLabel" />
                    <div class="form-group" data-court-type="SUPREME" >
                        <label for="select-search-judgment-form" class="col-xs-12 field-label"><c:out value="${scJudgmentFormLabel}"/>:</label>
                        <div class="col-xs-12">
                            <select name="scJudgmentFormId" id="select-search-judgment-form" class="form-control" data-field-desc="${scJudgmentFormLabel}: " >
                                <option value=""><spring:message code="judgmentSearch.formField.chooseScJudgmentForm" /></option>
                                <c:forEach items="${scJudgmentForms}" var="judgmentForm" >
                                    <option value="${judgmentForm.id}" <c:if test="${judgmentCriteriaForm.scJudgmentFormId == judgmentForm.id}" >selected="selected"</c:if> >
                                        <c:out value="${judgmentForm.name}" />
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    
                    <%-- Judge name --%>
                    <spring:message code="judgmentSearch.formField.judge" var="judgeNameLabel" />
                    <div class="form-group" >
                        <label for="input-search-judge" class="col-xs-12 field-label"><c:out value="${judgeNameLabel}" />:</label>
                        <div class="col-xs-11">
                            <form:input path="judgeName" id="input-search-judge" class="form-control" data-field-desc="${judgeNameLabel}: " />
                        </div>
                    </div>
        
                    <%-- Legal bases --%>
                    <div class="form-group">
                        <spring:message code="judgmentSearch.formField.legalBases" var="legalBasesLabel" />
                        <label for="input-search-legalbases" class="col-xs-12 field-label"><spring:message code="judgmentSearch.formField.legalBases" />:</label>
                        <div class="col-xs-11">
                            <form:input path="legalBase" class="form-control" id="input-search-legalbases" data-field-desc="${legalBasesLabel}: " />
                        </div>
                        <spring:message code="judgmentSearch.hint.legalBases.title" var="hintLegalBasesTitle" />
                        <spring:message code="judgmentSearch.hint.legalBases.content" var="hintLegalBasesContent" />
                        <div >
                            <saos:hint title="${hintLegalBasesTitle}" content="${hintLegalBasesContent}" placement="left" />
                        </div>
                    </div>
                    
                    
                    <%-- Law journal entry --%>
                    <div class="form-group law-journal">
                        <label for="lawJournalEntryCode" class="col-xs-12 field-label"><spring:message code="judgmentSearch.formField.lawJournalEntry" /></label>
                        <div class="col-xs-12">
                             
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
                                 
                                <button type="button" id="law-journal-set" class="btn btn-primary button btn-sm"><spring:message code="judgmentSearch.formField.lawJournal.set" /></button> 
                             </div>
                            
                            <div id="law-journal-fields" class="well col-sm-12" >
                                
                                <spring:message code="judgmentSearch.formField.lawJournal.close" var="lawJournalClose" />
                                <a class="remove law-close" href="" id="law-journal-close" title="${lawJournalClose}" data-placement="top" data-toggle="tooltip" ></a>
                                
                            <div class="row">
                            
                                <div class="col-xs-12">
                                    
                                    <div >
                                        <spring:message code="judgmentSearch.formField.lawJournal.year" />/
                                        <spring:message code="judgmentSearch.formField.lawJournal.journalNo" />/
                                        <spring:message code="judgmentSearch.formField.lawJournal.entry" />
                                    </div>
                                    <div >
                                        <input class="form-control" id="law-journal-year" type="text" size="4" />/
                                        <input class="form-control" id="law-journal-journalNo" type="text" size="4" />/
                                        <input class="form-control" id="law-journal-entry" type="text" size="4" />
                                    </div>
                                </div>
                                
                                <div class="col-xs-12">
                                                            
                                    <div >
                                        <spring:message code="judgmentSearch.formField.lawJournal.text" />:
                                    </div>
                                        
                                    <div >
                                        <input class="form-control width-full" id="law-journal-text" type="text" />
                                    </div>
                                </div>
                            
                            </div>
                                
                                <div class="col-xs-12">
                                    <ul id="law-journal-list" ></ul>
                                    <a href="" id="law-journal-more" class="law-journal-more display-none" ><spring:message code="more" /></a>
                                </div>
                                
                            </div>
                            
                        </div>
                        <spring:message code="judgmentSearch.formField.lawJournal.desc" var="lawJournalLabel" />
                        <form:hidden path="lawJournalEntryCode" id="lawJournalEntryCode" data-field-desc="${lawJournalLabel}: " />
                    </div>
                    
                    
                    <div id="common-court-fields" class="fields-container" >
                    
                        <%-- COMMON Keywords --%>
                        <spring:message code="judgmentSearch.formField.keywords" var="keywordsLabel" />
                        <div class="form-group" data-court-type="COMMON">
                            <label for="input-search-keywords-cc" class="col-xs-12 field-label"><c:out value="${keywordsLabel}" />:</label>
                            <div class="col-xs-11">
                                <form:input path="keywords" class="form-control" id="input-search-keywords-cc" data-field-desc="${keywordsLabel}: " />
                            </div>
                            <spring:message code="judgmentSearch.hint.ccKeywords.title" var="hintCcKeywordsTitle" />
                            <spring:message code="judgmentSearch.hint.ccKeywords.content" var="hintCcKeywordsContent" />
                            <div >
                                <saos:hint title="${hintCcKeywordsTitle}" content="${hintCcKeywordsContent}" placement="left" />
                            </div>
                        </div>
                    </div>
                     
                    <div id="supreme-court-fields" class="fields-container" >
                        
                    <%-- SUPREME PersonnelType --%>
                    <spring:message code="judgmentSearch.formField.personnelType" var="personnelTypeLabel" />
                    <div class="form-group" data-court-type="SUPREME" >
                        <label for="select-search-personnel-type" class="col-xs-12 field-label"><c:out value="${personnelTypeLabel}" />:</label>
                        <div class="col-xs-12">
                            <select name="scPersonnelType" class="form-control" id="select-search-personnel-type" data-field-desc="${personnelTypeLabel}: " >
                                <option value=""><spring:message code="judgmentSearch.formField.chooseScPersonnelType" /></option>
                                <saos:enumOptions enumType="pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType" selected="${judgmentCriteriaForm.scPersonnelType}" />
                            </select>
                        </div>
                    </div>
                    
                    </div>
                    
                    <div data-court-type="CONSTITUTIONAL_TRIBUNAL" >
                        
                        <%-- CT Dissenting Opinion --%>
                        <spring:message code="judgmentSearch.formField.ctDissentingOpinion" var="ctDissentingOpinionLabel" />
                        <div class="form-group" >
                            <label for="input-constitutional-tribunal-dissenting-opinion" class="col-xs-12 field-label"><c:out value="${ctDissentingOpinionLabel}" />:</label>
                            <div class="col-xs-11">
                                <form:input path="ctDissentingOpinion" id="input-constitutional-tribunal-dissenting-opinion" class="form-control" data-field-desc="${ctDissentingOpinionLabel}: " />
                            </div>
                        </div>
                        
                    </div>
                    
                    <div class="align-right">
                        <button id="judgment-clear-form-button" class="btn btn-xxs button btn-secondary"><spring:message code="clear" /></button>
                        <button id="confirm-section-judgment-info" class="btn btn-xxs button"><spring:message code="close" /></button>
                    </div>
                     
                </div>
    
        </div>
        
        <div class="form-group">
           <spring:message code="judgmentSearch.formField.all" var="searchAllPlaceholder" />
           <div class="col-lg-12 col-sm-12 col-xs-12">
               <form:input path="all" class="form-control search-all" id="input-search-all" placeholder="${searchAllPlaceholder}"  />
           </div>
           <div class="col-xs-12 align-right" >
               <button type="submit" class="btn btn-primary button"><spring:message code="button.judgmentSearch" /></button>
            </div>
        </div>
    
    </fieldset>
    
</div>

