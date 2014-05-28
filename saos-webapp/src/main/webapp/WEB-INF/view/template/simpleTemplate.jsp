<%@page pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>


<html>


<head>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" scope="session"></c:set>  
    <%@include file="/WEB-INF/view/common/cssInit.jsp"%>

</head>

<body>
    <tiles:insertAttribute name="content" flush="false" />
</body>
</html>