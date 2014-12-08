<%@page pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>


<html>


<head>
	<%@ include file="../common/includeCssJsLibs.jsp" %>
</head>

<body>
    <tiles:insertAttribute name="content" flush="false" />
</body>
</html>