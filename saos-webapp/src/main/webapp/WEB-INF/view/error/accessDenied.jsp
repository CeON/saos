<%@page pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>


<html>


<head>
   	<spring:eval expression="@exposedProperties.getProperty('webapp.version')" var="webappVersion" />
    
    <saos:cssjsInit></saos:cssjsInit>

</head>

<body>
    
    <div class="alert alert-danger"><spring:message code="error.accessDenied"/></div>

</body>
</html> 