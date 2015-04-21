<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="form-section-container" >

    <a id="court-info" class="form-section-open" href="" >
        <spring:message code="judgmentSearch.formField.courtType" />: <b><spring:message code="judgmentSearch.formField.courtTypeAny" /></b>
    </a>
    
    <div id="court-form-section" class="row form-section">
        
        <div class="form-group" >
        
            <label class="col-lg-2 col-sm-3 control-label"><spring:message code="judgmentSearch.formField.courtType" />:</label>    
            <div class="col-xs-10 radio-group" >
                <div class="col-sm-5" >
                    <input type="radio" id="radio-all" name="courtType"  value="" checked="checked" data-field-desc="<spring:message code="judgmentSearch.formField.courtType" />: " />
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
		            <c:if test="${fn:length(commonCourts)>1}">
		                <c:set var="emptyOption"><spring:message code="choose"/></c:set>
		                <option value=""><spring:message code="judgmentSearch.formField.chooseCommonCourt" text="${emptyOption}" /></option>
		            </c:if>
		        
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
		            <c:if test="${fn:length(commonCourtDivisions)>1}">
		                <c:set var="emptyOption"><spring:message code="choose"/></c:set>
		                <option value=""><spring:message code="judgmentSearch.formField.chooseCcDivision" text="${emptyOption}" /></option>
		            </c:if>
		        
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
		            <c:if test="${fn:length(supremeChambers)>1}">
		                <c:set var="emptyOption"><spring:message code="choose"/></c:set>
		                <option value=""><spring:message code="judgmentSearch.formField.chooseSupremeChamber" text="${emptyOption}" /></option>
		            </c:if>
		        
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
		            <c:if test="${fn:length(supremeChamberDivisions)>1}">
		                <c:set var="emptyOption"><spring:message code="choose"/></c:set>
		                <option value=""><spring:message code="judgmentSearch.formField.chooseScDivision" text="${emptyOption}" /></option>
		            </c:if>
		        
		            <c:forEach items="${supremeChamberDivisions}" var="item" >
		                <option value="${item.id}" <c:if test="${judgmentCriteriaForm.supremeChamberDivisionId == item.id}" >selected="selected"</c:if> >
		                    <c:out value="${item.name}" />
		                </option>
		            </c:forEach>
		        </form:select>
		    </div>
		</div>
        
    </div>
    
</div>