<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="pagePagination" description="Search result page bookmarks" small-icon=""%>

<%@ attribute name="pageNo" required="true" description="The page link without pageNo parameter" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="totalPages" required="true" description="The page link without pageNo parameter" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="pageLink" required="true" description="The page link without pageNo parameter" rtexprvalue="true" type="java.lang.String"%>


<c:set var="pageNo" value="${pageNo+1}" />
<c:set var="pageBlocks" value="${totalPages}" />
<c:set var="pageBlockNumber" value="8" />

<c:if test="${totalPages > pageBlockNumber }" >
	<c:set var="pageBlocks" value="${pageBlockNumber+1}" />
</c:if>

<fmt:formatNumber var="halfBookmarks" value="${pageBlockNumber/2}" maxFractionDigits="0" scope="page"/>

<c:set var="begin" value="1" />
<c:set var="end" value="${pageBlocks}" />

<c:if test="${pageNo - halfBookmarks > 0 }" >
	<c:set var="begin" value="${pageNo - halfBookmarks}" />
	<c:set var="end" value="${pageNo + halfBookmarks}" />
</c:if>


<c:if test="${pageNo + halfBookmarks + 1 > totalPages && totalPages > pageBlockNumber}" >
	<c:set var="begin" value="${totalPages - pageBlockNumber}" />
	<c:set var="end" value="${totalPages}" />
</c:if>


<c:if test="${totalPages > 0}">     
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
		
		<c:if test="${totalPages > 0}" >
			<div class="results-number">
				<spring:message code="pagination.pages" arguments="${pageNo},${totalPages}" />
			</div>
		</c:if>
	
    </div>
</c:if>
