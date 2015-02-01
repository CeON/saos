<%@page pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="alert alert-danger"><spring:message code="error.internal" /></div>
     
<!-- 
	<% 
		if (exception == null) {
			exception = (Throwable)request.getAttribute("javax.servlet.error.exception");
		}
		org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("pl.edu.icm.crpd.webapp.jsp");
		logger.error("", exception);
		exception.printStackTrace(new java.io.PrintWriter(out));
	%>
-->

