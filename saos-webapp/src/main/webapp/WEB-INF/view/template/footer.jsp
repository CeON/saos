<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<div class="container">

	<ul class="nav navbar-nav" >
		<li><spring:message code="saos.fullname"/></li>
		<li><spring:eval expression="@versionProperties.getProperty('saos.version')"/></li>
	</ul>
	
	<ul class="nav navbar-nav navbar-right" >
		<li><spring:message code="header.navigation.home" /></li>
		<li><spring:message code="header.navigation.search" /></li>	
		<li><spring:message code="header.navigation.contact" /></li>
	</ul>

</div>

