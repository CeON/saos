<%@page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<!DOCTYPE html>


<html>


<c:set var="contextPath" value="${pageContext.request.contextPath}" scope="session"></c:set>


<head>
	<meta charset="UTF-8" />
	<saos:cssJsInit></saos:cssJsInit>
</head>

<body>
    <%@ include file="piwik.jsp" %>
    
    <tiles:insertAttribute name="content" flush="false" />
</body>
</html>