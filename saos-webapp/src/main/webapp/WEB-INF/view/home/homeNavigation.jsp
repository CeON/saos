<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<h1>
    <a href="${contextPath}/" class="saos-logo-link">
        <img class="saos-logo" src="${contextPath}/static/image/saosLogo.png" alt="<spring:message code="saos.fullname"/> - <spring:message code='saos.logo.alt' />" />
    </a>
    
    <a href="${contextPath}/" class="saos-logo-mobile-link">
        <img class="saos-logo-mobile" src="${contextPath}/static/image/saosLogoMobile.png" alt="<spring:message code="saos.fullname"/> - <spring:message code='saos.logo.alt' />" />
    </a>    
</h1>

   
<h2 class="welcome-text">
   <spring:message code="home.welcome.text" />
</h2>
                
                