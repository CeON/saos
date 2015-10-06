<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<c:set var="pageContextRequest" value="${pageContext.request}" />
<c:set var="requestURL">${pageContextRequest.requestURL}</c:set>
<c:set var="requestURI" value="${pageContextRequest.requestURI}" />

<%--Base url --%>
<c:set var="basePath" value="${fn:substring(requestURL, 0, fn:length(requestURL) - fn:length(requestURI))}${pageContextRequest.contextPath}" />

<%-- Open graph ogp.me --%>
<meta property="og:site_name" content="Saos">
<meta property="og:type" content="object">
<meta property="og:image" content="${basePath}/static/image/saosLogo.png">
<meta property="og:url" content="${basePath}${requestScope['javax.servlet.forward.servlet_path']}">
 
