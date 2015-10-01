<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<h1>
    <a href="${contextPath}/" class="saos-logo-link" title="<spring:message code='linkToMainPage'/>" aria-label="<spring:message code='linkToMainPage'/>" >
        <img class="saos-logo" src="${contextPath}/static/image/saosLogoNoText.png" alt="<spring:message code="saos.fullname"/> - <spring:message code='saos.logo.alt' />" />
    </a>
    
    <a href="${contextPath}/" class="saos-logo-mobile-link" title="<spring:message code='linkToMainPage'/>" aria-label="<spring:message code='linkToMainPage'/>">
        <img class="saos-logo-mobile" src="${contextPath}/static/image/saosLogoMobile.png" alt="<spring:message code="saos.fullname"/> - <spring:message code='saos.logo.alt' />" />
    </a>    
</h1>
