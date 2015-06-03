<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


    
    <spring:nestedPath path="${courtCriteriaNestedPath}">

    <div id="court-form-section" class="row form-section">
        
        <div class="form-group" >
        
            <div class="col-xs-10 radio-group" >
                <div class="col-xs-12" >
                    <input type="radio" id="courtType_ALL" name="${courtCriteriaNestedPath}.courtType" value="" checked="checked" />
                    <label for="courtType_ALL" class="radio-label" >
                        <spring:message code="courtCriteriaFormSection.formField.courtTypeAny" />
                    </label>
                </div>
                
                <spring:eval expression="T(pl.edu.icm.saos.persistence.model.CourtType).values()" var="enumItemsToShow" scope="page"/>
                <c:set var="path" value="courtType" />
                
                <c:forEach var="enumValue" items="${enumItemsToShow}" >
                    <c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
                    
                    <c:choose>
                        <c:when test="${enumValue == 'ADMINISTRATIVE'}">        
                            <div class="col-xs-12" >
                                <form:radiobutton id="courtType_ADMINISTRATIVE" path="${path}" value="${enumValue}" disabled="true" />
                                <label for="courtType_${enumValue}" class="radio-label radio-label-disabled" >
                                    <saos:enum value="${enumValue}" />
                                </label>
                                
                                <!-- Hint for administrative court -->
                                <spring:message code="courtCriteriaFormSection.hint.administrativeCourt.title" var="hintAdministrativeCourtTitle" />
                                <spring:message code="courtCriteriaFormSection.hint.administrativeCourt.content" var="hintAdministrativeCourtContent" />
                                <saos:hint title="${hintAdministrativeCourtTitle}" content="${hintAdministrativeCourtContent}" />
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="col-xs-12" >
                                <form:radiobutton path="${path}" id="courtType_${enumValue}" value="${enumValue}" />
                                <label for="courtType_${enumValue}" class="radio-label" >
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
		    <label for="select-common-court" class="col-xs-12"><spring:message code="courtCriteriaFormSection.formField.commonCourt"/>:</label>
		    <div class="col-xs-12 ">
		        
		        <form:select path="ccCourtId" id="select-common-court" class="form-control" disabled="${fn:length(commonCourts)==0}" >
		            
	                <option value=""><spring:message code="courtCriteriaFormSection.formField.chooseCommonCourt" /></option>
		            
		            <c:forEach items="${commonCourts}" var="court" >
		                <option value="${court.id}" data-cc-court-type="${court.type}" 
		                      <c:if test="${courtCriteria.ccCourtId == court.id}" >selected="selected"<c:set var="selectedCourtType" value="${court.type}"/></c:if> >
		                    <c:out value="${court.name}" />
		                </option>
		            </c:forEach>
		        </form:select>
		    </div>
		</div>
		
		<div class="form-group" data-court-type="COMMON" >
            <div class="col-xs-12">
	            <spring:message code='courtCriteriaFormSection.formField.ccIncludeDependentCourtJudgments.infoSectionCustomText' var="infoSectionText"/>
	            <form:checkbox path="ccIncludeDependentCourtJudgments" id="ccIncludeDependentCourtJudgments" data-info-section-custom-text="${infoSectionText}" disabled="${fn:length(commonCourts)==0 || selectedCourtType == 'DISTRICT'}"/>
	            <label for="ccIncludeDependentCourtJudgments">
	                <spring:message code="courtCriteriaFormSection.formField.ccIncludeDependentCourtJudgments"/>
	            </label>
            </div>
        </div>
        
		
		
		<%-- Common Court Divisions --%>
		<div class="form-group" data-court-type="COMMON" >
		    <label for="select-common-division" class="col-xs-12"><spring:message code="courtCriteriaFormSection.formField.commonDivision" />:</label>
		    <div class="col-xs-12">
		        
		        <form:select path="ccCourtDivisionId" id="select-common-division" class="form-control" disabled="${fn:length(commonCourtDivisions)==0 || courtCriteria.ccIncludeDependentCourtJudgments==true}" >
		            
	                <option value=""><spring:message code="courtCriteriaFormSection.formField.chooseCcDivision" /></option>
		        
		            <c:forEach items="${commonCourtDivisions}" var="item" >
		                <option value="${item.id}" <c:if test="${courtCriteria.ccCourtDivisionId == item.id}" >selected="selected"</c:if> >
		                    <c:out value="${item.name}" />
		                </option>
		            </c:forEach>
		        </form:select>
		    </div>
		</div>

        
        <%-- Supreme CourtChamber --%>
		<div class="form-group" data-court-type="SUPREME" >
		    <label for="select-supreme-chamber" class="col-xs-12"><spring:message code="courtCriteriaFormSection.formField.supremeChamber" />:</label>
		    <div class="col-xs-12">
		        
		        <form:select path="scCourtChamberId" id="select-supreme-chamber" class="form-control" disabled="${fn:length(supremeChambers)==0}" >
		            
	                <option value=""><spring:message code="courtCriteriaFormSection.formField.chooseSupremeChamber" /></option>
		        
		            <c:forEach items="${supremeChambers}" var="item" >
		                <option value="${item.id}" <c:if test="${courtCriteria.scCourtChamberId == item.id}" >selected="selected"</c:if> >
		                    <c:out value="${item.name}" />
		                </option>
		            </c:forEach>
		        </form:select>
		    </div>
		</div>
		
		<%-- Supreme ChamberDivisions --%>
		<div class="form-group" data-court-type="SUPREME" >
		    <label for="select-supreme-chamber-division" class="col-xs-12"><spring:message code="courtCriteriaFormSection.formField.supremeChamberDivision" />:</label>
		    <div class="col-xs-12">
		        
		        <form:select path="scCourtChamberDivisionId" id="select-supreme-chamber-division" class="form-control" disabled="${fn:length(supremeChamberDivisions)==0}" >
		            
	                <option value=""><spring:message code="courtCriteriaFormSection.formField.chooseScDivision" /></option>
		        
		            <c:forEach items="${supremeChamberDivisions}" var="item" >
		                <option value="${item.id}" <c:if test="${courtCriteria.scCourtChamberDivisionId == item.id}" >selected="selected"</c:if> >
		                    <c:out value="${item.name}" />
		                </option>
		            </c:forEach>
		        </form:select>
		    </div>
		</div>
		
        <div class="align-right">
            <button class="btn btn-sm button confirm-section-button"><spring:message code="confirm" /></button>
        </div>
        
    </div>
    
    </spring:nestedPath>
