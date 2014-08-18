<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<spring:eval expression="@exposedProperties.getProperty('webapp.version')" var="webappVersion" />
   
   <c:choose>
   	<c:when test="${webappVersion=='development'}" >
   		<%@include file="/WEB-INF/view/common/javaScriptInitDev.jsp"%>
   		<%@include file="/WEB-INF/view/common/cssInitDev.jsp"%>
   	</c:when>
   	<c:when test="${webappVersion=='production'}" >
   		<%@include file="/WEB-INF/view/common/javaScriptInitProd.jsp"%>
   		<%@include file="/WEB-INF/view/common/cssInitProd.jsp"%>
   	</c:when>
   	<c:otherwise>
   		<%@include file="/WEB-INF/view/common/javaScriptInitDev.jsp"%>
   		<%@include file="/WEB-INF/view/common/cssInitDev.jsp"%>
   	</c:otherwise>
   </c:choose>
