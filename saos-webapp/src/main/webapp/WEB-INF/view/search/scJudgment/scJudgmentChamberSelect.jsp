<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>                
                


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

