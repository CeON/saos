<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<c:choose>
	<%-- Common court judgment view --%>
	<c:when test="${judgment.instanceOfCommonCourtJudgment}">
		<%@ include file="ccJudgment/ccJudgmentDetails.jsp" %>
	</c:when>
	
	<%-- Supreme court judgment view --%>
	<c:when test="${judgment.instanceOfSupremeCourtJudgment}">
		<%@ include file="scJudgment/scJudgmentDetails.jsp" %>
	</c:when>
	
	<%-- Administrative court judgment view --%>
	<c:when test="${judgment.instanceOfAdministrativeCourtJudgment}">
		
	
	</c:when>
	
	<c:otherwise>
	
	</c:otherwise>
</c:choose>

