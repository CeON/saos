<%@page pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<!DOCTYPE html>


<html>


<head>
	<meta charset="UTF-8" />
	<saos:cssJsInit></saos:cssJsInit>
</head>

<body>
    <%@ include file="piwik.jsp" %>
    
    <tiles:insertAttribute name="content" flush="false" />
</body>
</html>