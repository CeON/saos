<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="pagePagination" description="Search result page bookmarks" small-icon=""%>

<%@ attribute name="pageNo" required="true" description="Number of page" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="pageSize" required="true" description="Maximum results on single page" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="resultsNo" required="true" description="Number of all results" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="pageLink" required="true" description="The page link without pageNo parameter" rtexprvalue="true" type="java.lang.String"%>


<c:set var="pageNo" value="${pageNo+1}" />
<c:set var="pageBlockNumber" value="8" />

<fmt:formatNumber var="halfBookmarks" value="${pageBlockNumber/2}" maxFractionDigits="0" scope="page"/>
<fmt:formatNumber var="totalPages" value="${resultsNo/pageSize + 0.5}" maxFractionDigits="0" pattern="#" scope="page" />

<c:set var="begin" value="${pageNo - halfBookmarks }" />
<c:set var="end" value="${pageNo + halfBookmarks}" />

<c:if test="${begin < 1 }">
	<c:set var="offset" value="${-begin + 1 }" />
	<c:set var="begin" value="1" />
	<c:set var="end" value="${(end + offset > totalPages) ? totalPages : end + offset }" />
</c:if>

<c:if test="${end > totalPages }">
	<c:set var="offset" value="${end - totalPages }" />
	<c:set var="begin" value="${(begin - offset < 1) ? 1 : begin - offset }" />
	<c:set var="end" value="${totalPages}" />
</c:if>


<c:if test="${totalPages > 1}">     
    <div class="pagination"> 
	
		<div>
			<c:if test="${pageNo > 1}">
		        <a class="page-block" href="${pageLink}&amp;page=1"><spring:message code="pagination.first" /><span><spring:message code="pagination.page.desc" /></span></a>
		        <a class="page-block" href="${pageLink}&amp;page=${pageNo-1}"><spring:message code="pagination.previous" /><span><spring:message code="pagination.page.desc" /></span></a>
		    </c:if>
		
			<c:forEach begin="${begin}" end="${end}" varStatus="status" >
				<a href="${pageLink}&amp;page=${status.index}" class="page-block ${status.index == pageNo ? 'active' : ''}"><c:out value="${status.index}" /><span><spring:message code="pagination.page.desc" /></span></a>
			</c:forEach>
			
			<c:if test="${pageNo < totalPages}">
				<a class="page-block" href="${pageLink}&amp;page=${pageNo+1}"><spring:message code="pagination.next" /><span><spring:message code="pagination.page.desc" /></span></a>
				<a class="page-block" href="${pageLink}&amp;page=${totalPages}"><spring:message code="pagination.last" /><span><spring:message code="pagination.page.desc" /></span></a>
			</c:if>
		</div>
		
		<div class="results-number">
			<spring:message code="pagination.pages" arguments="${pageNo},${totalPages}" />
		</div>
	
    </div>
</c:if>
