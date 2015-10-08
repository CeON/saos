<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<spring:eval expression="T(pl.edu.icm.saos.common.http.HttpServletRequestUtils).constructRequestBaseUrl(pageContext.request)" var="baseUrl" />

<%-- Open graph ogp.me --%>
<meta property="og:site_name" content="Saos">
<meta property="og:type" content="website">
<meta property="og:image" content="${baseUrl}${contextPath}/static/image/saosLogo.png">
<meta property="og:url" content="${requestUrlWithParameters}">
 
 