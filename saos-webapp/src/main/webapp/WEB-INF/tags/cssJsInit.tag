<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<spring:eval expression="@exposedProperties.getProperty('webapp.distribution')" var="webappDistribution" />
   
<%@include file="/WEB-INF/view/common/cssFonts.jsp"%>
   
<c:choose>
	<c:when test="${webappDistribution == 'development'}" >
		<%@include file="/WEB-INF/view/common/javaScriptInitDev.jsp"%>
		<%@include file="/WEB-INF/view/common/cssInitDev.jsp"%>
	</c:when>
	<c:when test="${webappDistribution == 'production'}" >
		<%@include file="/WEB-INF/view/common/javaScriptInitProd.jsp"%>
		<%@include file="/WEB-INF/view/common/cssInitProd.jsp"%>
	</c:when>
	<c:otherwise>
		<%@include file="/WEB-INF/view/common/javaScriptInitDev.jsp"%>
		<%@include file="/WEB-INF/view/common/cssInitDev.jsp"%>
	</c:otherwise>
</c:choose>

