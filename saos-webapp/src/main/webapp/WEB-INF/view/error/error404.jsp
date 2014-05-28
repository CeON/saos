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
    <div class="alert alert-danger"><spring:message code="pageNotFound"/></div>
</body>
</html>