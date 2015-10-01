<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>




<h2 class="content-header" ><spring:message code="judgmentSearch.results.header" /><span><spring:message code="judgmentSearch.results.judgmentsNumber" arguments="${resultsNo}" /></span></h2>

<div class="row pagination-row">

    <c:if test="${resultsNo > 0}">
    
        <div class="settings-box" id="settings-box" >
            <fieldset>
                <legend><spring:message code="sort.title" /></legend>
                
                <div class="col-sm-3 col-md-8 col-lg-3" >    
                    <label class="label" for="searchPageSize" ><spring:message code="sort.pageSize" />:</label>
                    <select id="searchPageSize" name="size">
                        <option value="10" <c:if test="${pageSize==10}"> selected="selected"</c:if>>10</option>
                        <option value="20" <c:if test="${pageSize==20}"> selected="selected"</c:if>>20</option>
                        <option value="50" <c:if test="${pageSize==50}"> selected="selected"</c:if>>50</option>
                        <option value="100" <c:if test="${pageSize==100}"> selected="selected"</c:if>>100</option>
                    </select>
                </div>
                
                <div class="col-sm-5 col-md-8 col-lg-5" >
                    <c:set var="sortDirectionValue" value=",${fn:toLowerCase(sortDirection)}"/>
                    
                    <label class="label" for="searchSorting" ><spring:message code="sort.by" />:</label>
                    <select id="searchSorting" name="sort">
                        <option value="RELEVANCE${sortDirectionValue}" <c:if test="${sortProperty=='RELEVANCE'}"> selected="selected"</c:if> ><spring:message code="sort.accuracy" /></option>
                        <option value="JUDGMENT_DATE${sortDirectionValue}" <c:if test="${sortProperty == 'JUDGMENT_DATE'}"> selected="selected"</c:if> ><spring:message code="sort.date" /></option>
                        <option value="REFERENCING_JUDGMENTS_COUNT${sortDirectionValue}" <c:if test="${sortProperty=='REFERENCING_JUDGMENTS_COUNT'}"> selected="selected"</c:if> ><spring:message code="sort.referencingCount" /></option>
                        <option value="MAXIMUM_MONEY_AMOUNT${sorDirectionValue}" <c:if test="${sortProperty == 'MAXIMUM_MONEY_AMOUNT'}"> selected="selected"</c:if> ><spring:message code="sort.maxMoneyAmount" /></option>
                    </select>
                    
                    <label for="searchSortingDirection" ><spring:message code="sort.direction" />:</label>
                    <input id="searchSortingDirection" type="checkbox" value="checked" <c:if test="${sortDirection == 'ASC'}"> checked="checked" </c:if> />
                </div>
            </fieldset>
        </div>
    
    </c:if>
    
    
    <saos:pagePagination pageLink="${pageLink}" pageNo="${pageNo}" pageSize="${pageSize}" resultsNo="${resultsNo}" ></saos:pagePagination>

</div>

<div id="judgment-list" class="judgment-list"> 


    <%@ include file="judgmentList.jsp" %>
    
    
    <c:if test="${resultsNo == 0}">
        <div class="no-results">
            <p>
               <spring:message code="judgmentSearch.results.noRecords" />
            </p>
            <p>
               <spring:message code="judgmentSearch.results.hints" />:
            </p>
            <ul>
               <li><spring:message code="judgmentSearch.results.hints.first" />,</li>
               <li><spring:message code="judgmentSearch.results.hints.second" />.</li>
            </ul>
            
        </div>
    </c:if>

</div>

<div class="row pagination-row">

    <saos:pagePagination pageLink="${pageLink}" pageNo="${pageNo}" pageSize="${pageSize}" resultsNo="${resultsNo}" ></saos:pagePagination>

</div>

