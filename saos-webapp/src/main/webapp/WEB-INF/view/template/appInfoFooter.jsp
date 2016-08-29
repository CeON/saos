<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<div class="app-info-footer">
    <spring:message code="appInfo.contact"/>: <spring:eval var="contactMailAddress" expression="@exposedProperties.getProperty('contact.generalMailAddress')"/>
    <saos:mail value="${contactMailAddress}"/> 
    | 
    <spring:message code="appInfo.saosVersion"/>: <spring:eval expression="@versionProperties.getProperty('saos.version')"/>
</div>