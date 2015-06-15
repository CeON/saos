<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<div class="nav-menu" id="nav-menu" tabindex=0>

    <a id="menu-button" class="menu-button" href="" >
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
    </a>

    <ul id="navigation-links" >
        <li ><a href="${contextPath}/search"><spring:message code="navigation.search" /></a></li>
        <li ><a href="${contextPath}/analysis"><spring:message code="navigation.analysis" /></a></li>
        <li ><a href="${contextPath}/api"><spring:message code="navigation.api" /></a></li>
        <li ><a href="${contextPath}/help"><spring:message code="navigation.help" /></a></li>
      </ul>

</div>

