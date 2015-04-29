<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


    
    <spring:nestedPath path="${courtCriteriaNestedPath}">

    <div id="court-form-section" class="row form-section">
        
        <div class="form-group" >
        
            <label class="col-lg-2 col-sm-3 control-label"><spring:message code="judgmentSearch.formField.courtType" />:</label>    
            <div class="col-xs-10 radio-group" >
                <div class="col-sm-5" >
                    <input type="radio" id="courtType_ALL" name="${courtCriteriaNestedPath}.courtType"  value="" checked="checked"  data-field-desc="<spring:message code="context.court.fieldDescription" />: " />
                    <label for="courtType_ALL" ><spring:message code="judgmentSearch.formField.courtTypeAny" /></label>
                </div>
                
                <spring:eval expression="T(pl.edu.icm.saos.persistence.model.CourtType).values()" var="enumItemsToShow" scope="page"/>
                <c:set var="path" value="courtType" />
                
                <c:forEach var="enumValue" items="${enumItemsToShow}" >
                    <c:set var="lowerCaseEnumValue" value="${fn:toLowerCase(enumValue)}" />
                    
                    <c:choose>
                        <c:when test="${enumValue == 'ADMINISTRATIVE'}">        
                            <div class="col-sm-6" >
                                <form:radiobutton id="courtType_ADMINISTRATIVE" path="${path}" value="${enumValue}" disabled="true" />
                                <label for="courtType_${enumValue}" >
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
                                <form:radiobutton path="${path}" id="courtType_${enumValue}" value="${enumValue}" />
                                <label for="courtType_${enumValue}" >
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
		        
		        <form:select path="ccCourtId" id="select-common-court" class="form-control" disabled="${fn:length(commonCourts)==0}" >
		            
	                <option value=""><spring:message code="judgmentSearch.formField.chooseCommonCourt" /></option>
		            
		            <c:forEach items="${commonCourts}" var="item" >
		                <option value="${item.id}" <c:if test="${courtCriteria.ccCourtId == item.id}" >selected="selected"</c:if> >
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
		        
		        <form:select path="ccCourtDivisionId" id="select-common-division" class="form-control" disabled="${fn:length(commonCourtDivisions)==0}" >
		            
	                <option value=""><spring:message code="judgmentSearch.formField.chooseCcDivision" /></option>
		        
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
		    <label for="select-supreme-chamber" class="col-lg-2 col-sm-3 col-xs-12 control-label"><spring:message code="judgmentSearch.formField.supremeChamber" />:</label>
		    <div class="col-lg-7 col-sm-8 col-xs-11">
		        
		        <form:select path="scCourtChamberId" id="select-supreme-chamber" class="form-control" disabled="${fn:length(supremeChambers)==0}" >
		            
	                <option value=""><spring:message code="judgmentSearch.formField.chooseSupremeChamber" /></option>
		        
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
		    <label for="select-supreme-chamber-division" class="col-lg-2 col-sm-3 col-xs-12 control-label"><spring:message code="judgmentSearch.formField.supremeChamberDivision" />:</label>
		    <div class="col-lg-7 col-sm-8 col-xs-11">
		        
		        <form:select path="scCourtChamberDivisionId" id="select-supreme-chamber-division" class="form-control" disabled="${fn:length(supremeChamberDivisions)==0}" >
		            
	                <option value=""><spring:message code="judgmentSearch.formField.chooseScDivision" /></option>
		        
		            <c:forEach items="${supremeChamberDivisions}" var="item" >
		                <option value="${item.id}" <c:if test="${courtCriteria.scCourtChamberDivisionId == item.id}" >selected="selected"</c:if> >
		                    <c:out value="${item.name}" />
		                </option>
		            </c:forEach>
		        </form:select>
		    </div>
		</div>
        
    </div>
    
    </spring:nestedPath>
