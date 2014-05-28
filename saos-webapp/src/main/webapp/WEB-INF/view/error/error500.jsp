<%@page pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>


<html>


<head>
    <%@include file="/WEB-INF/view/common/cssInit.jsp"%>

</head>

<body>
<div class="alert alert-danger">error occurred</div>
    <!-- 
	<% 
	   if (exception == null) {
	    exception = (Throwable)request.getAttribute("javax.servlet.error.exception");
	   }
	   org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("pl.edu.icm.crpd.webapp.jsp");
	   logger.error("", exception);
	   exception.printStackTrace();
	 %>
	 -->
	 
 </body>
 
 </html>