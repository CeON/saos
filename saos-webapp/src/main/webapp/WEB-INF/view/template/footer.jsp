<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<div class="container">

	<ul class="nav navbar-nav" id="saos-version" >
		<li>
			<a href="${contextPath}/" >
			<spring:message code="saos.fullname"/> - 
			<spring:eval expression="@versionProperties.getProperty('saos.version')"/>			
			</a>
		</li>
	</ul>
	
	<ul class="nav navbar-nav navbar-right" id="navigation-footer">
		<li><a href="${contextPath}/"><spring:message code="navigation.home" /></a></li>
		<li><a href="${contextPath}/search"><spring:message code="navigation.search" /></a></li>	
		<li><a href="#"><spring:message code="navigation.contact" /></a></li>
	</ul>

</div>

