<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

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

