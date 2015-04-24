<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="form-section-container" >

    <div class="context-bar" >
	    <a id="court-info" class="form-section-open" href="" >
	       <spring:message code="context.court.fieldDescription" />: <b><spring:message code="context.court.anyValue" /></b>
	    </a>
	    
	    <a id="date-info" class="form-section-open" href="" >
	       <spring:message code="context.date.fieldDescription" />: <b><spring:message code="context.date.anyValue" /></b>
	    </a>
    </div>
    
    <div id="court-form-section" class="row form-section">
        
        <div class="form-group" >
        
            <label class="col-lg-2 col-sm-3 control-label"><spring:message code="judgmentSearch.formField.courtType" />:</label>    
            <div class="col-xs-10 radio-group" >
                <div class="col-sm-5" >
                    <input type="radio" id="radio-all" name="courtType"  value="" checked="checked" data-field-desc="<spring:message code="context.court.fieldDescription" />: " />
                    <label for="radio-all" ><spring:message code="judgmentSearch.formField.courtTypeAny" /></label>
                </div>
                
                <spring:eval expression="T(pl.edu.icm.saos.persistence.model.CourtType).values()" var="enumItemsToShow" scope="page"/>
                <c:set var="path" value="courtType" />
                
                <c:forEach var="enumValue" items="${enumItemsToShow}" >
                    <c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
                    <c:set var="idLabel" value="radio-court-${lowerCaseEnumValue}" />
                
                    <c:choose>
                        <c:when test="${enumValue == 'ADMINISTRATIVE'}">        
                            <div class="col-sm-6" >
                                <form:radiobutton path="${path}" value="${enumValue}" id="${idLabel}" disabled="true" />
                                <label for="${idLabel}" >
                                    <saos:enum value="${enumValue}" />
                                </label>
                                
                                <!-- Hint for administrative court -->
                                <spring:message code="judgmentSearch.hint.administrativeCourt.title" var="hintAdministrativeCourtTitle" />
                                <spring:message code="judgmentSearch.hint.administrativeCourt.content" var="hintAdministrativeCourtContent" />
                                <saos:hint title="${hintAdministrativeCourtTitle}" content="${hintAdministrativeCourtContent}" />
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="col-sm-5" >
                                <form:radiobutton path="${path}" value="${enumValue}" id="${idLabel}" />
                                <label for="${idLabel}" >
                                    <saos:enum value="${enumValue}" />
                                </label>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </div>
        
        
        <%-- Common Courts --%>
		<div class="form-group" data-court-type="COMMON" >
		    <label for="select-common-court" class="col-lg-2 col-sm-3 col-xs-12 control-label"><spring:message code="judgmentSearch.formField.commonCourt"/>:</label>
		    <div class="col-lg-7 col-sm-8 col-xs-11">
		        
		        <form:select path="commonCourtId" id="select-common-court" class="form-control" disabled="${fn:length(commonCourts)==0}" >
		            
	                <option value=""><spring:message code="judgmentSearch.formField.chooseCommonCourt" /></option>
		            
		            <c:forEach items="${commonCourts}" var="item" >
		                <option value="${item.id}" <c:if test="${judgmentCriteriaForm.commonCourtId == item.id}" >selected="selected"</c:if> >
		                    <c:out value="${item.name}" />
		                </option>
		            </c:forEach>
		        </form:select>
		    </div>
		</div>
		
		<%-- Common Court Divisions --%>
		<div class="form-group" data-court-type="COMMON" >
		    <label for="select-common-division" class="col-lg-2 col-sm-3 col-xs-12 control-label"><spring:message code="judgmentSearch.formField.commonDivision" />:</label>
		    <div class="col-lg-7 col-sm-8 col-xs-11">
		        
		        <form:select path="commonCourtDivisionId" id="select-common-division" class="form-control" disabled="${fn:length(commonCourtDivisions)==0}" >
		            
	                <option value=""><spring:message code="judgmentSearch.formField.chooseCcDivision" /></option>
		        
		            <c:forEach items="${commonCourtDivisions}" var="item" >
		                <option value="${item.id}" <c:if test="${selectedItem == item.id}" >selected="selected"</c:if> >
		                    <c:out value="${item.name}" />
		                </option>
		            </c:forEach>
		        </form:select>
		    </div>
		</div>

        
        <%-- Supreme CourtChamber --%>
		<div class="form-group" data-court-type="SUPREME" >
		    <label for="select-supreme-chamber" class="col-lg-2 col-sm-3 col-xs-12 control-label"><spring:message code="judgmentSearch.formField.supremeChamber" />:</label>
		    <div class="col-lg-7 col-sm-8 col-xs-11">
		        
		        <form:select path="supremeChamberId" id="select-supreme-chamber" class="form-control" disabled="${fn:length(supremeChambers)==0}" >
		            
	                <option value=""><spring:message code="judgmentSearch.formField.chooseSupremeChamber" /></option>
		        
		            <c:forEach items="${supremeChambers}" var="item" >
		                <option value="${item.id}" <c:if test="${judgmentCriteriaForm.supremeChamberId == item.id}" >selected="selected"</c:if> >
		                    <c:out value="${item.name}" />
		                </option>
		            </c:forEach>
		        </form:select>
		    </div>
		</div>
		
		<%-- Supreme ChamberDivisions --%>
		<div class="form-group" data-court-type="SUPREME" >
		    <label for="select-supreme-chamber-division" class="col-lg-2 col-sm-3 col-xs-12 control-label"><spring:message code="judgmentSearch.formField.supremeChamberDivision" />:</label>
		    <div class="col-lg-7 col-sm-8 col-xs-11">
		        
		        <form:select path="supremeChamberDivisionId" id="select-supreme-chamber-division" class="form-control" disabled="${fn:length(supremeChamberDivisions)==0}" >
		            
	                <option value=""><spring:message code="judgmentSearch.formField.chooseScDivision" /></option>
		        
		            <c:forEach items="${supremeChamberDivisions}" var="item" >
		                <option value="${item.id}" <c:if test="${judgmentCriteriaForm.supremeChamberDivisionId == item.id}" >selected="selected"</c:if> >
		                    <c:out value="${item.name}" />
		                </option>
		            </c:forEach>
		        </form:select>
		    </div>
		</div>
        
    </div>
    
    <div id="date-form-section" class="row form-section">
        
        <spring:message code="judgmentSearch.formField.datePlaceholder" var="datePlaceholder" />
    
        <%-- Date selection --%>
		<div class="form-group ">
		    <label for="datepicker_from" class="col-lg-2 col-md-3 col-sm-3 col-xs-12 control-label"><spring:message code="judgmentSearch.formField.dateFrom" />:</label>
		    <div class="col-lg-2 col-md-2 col-sm-9 col-xs-12">
		        <label id="datepicker_from-error" class="valid-error" for="datepicker_from"><spring:message code="judgmentSearch.validation.date.error" /></label>
		        <form:input path="dateFrom" class="form-control form-date" id="datepicker_from" placeholder="${datePlaceholder}" maxlength="10" autocomplete="off" />
		        <label id="datepicker_from-desc" class="for-screen-readers" for="datepicker_from"><spring:message code="judgmentSearch.formField.dateWrongFormat" /></label>
		    </div>
		
		    <label for="datepicker_to" class="col-lg-2 col-md-2 col-sm-3 col-xs-12 control-label"><spring:message code="judgmentSearch.formField.dateTo" />:</label>
		    <div class="col-lg-2 col-md-2 col-sm-9 col-xs-12">
		        <label id="datepicker_to-error" class="valid-error" for="datepicker_to"><spring:message code="judgmentSearch.validation.date.error" /></label>
		        <form:input path="dateTo" class="form-control form-date" id="datepicker_to" placeholder="${datePlaceholder}" maxlength="10" autocomplete="off" />
		        <label id="datepicker_to-desc" class="for-screen-readers" for="datepicker_to"><spring:message code="judgmentSearch.formField.dateWrongFormat" /></label>
		    </div>
		</div>
    
    </div>
    
</div>

