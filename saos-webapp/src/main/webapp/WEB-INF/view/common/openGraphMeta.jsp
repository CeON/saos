<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<%-- Open graph ogp.me --%>
<spring:eval var="domainName" expression="@exposedProperties.getProperty('domain.name')"/>  

<meta property="og:site_name" content="Saos">
<meta property="og:type" content="object">
<meta property="og:image" content="${domainName}/static/image/saosLogo.png">
<meta property="og:url" content="${domainName}${requestScope['javax.servlet.forward.servlet_path']}">
 
