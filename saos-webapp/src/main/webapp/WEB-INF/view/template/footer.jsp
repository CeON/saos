<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<div class="container">

	<ul class="nav navbar-nav" >
		<li><spring:message code="saos.fullname"/></li>
		<li><spring:eval expression="@versionProperties.getProperty('saos.version')"/></li>
	</ul>
	
	<ul class="nav navbar-nav navbar-right" >
		<li>Co robimy</li>
		<li>Wyszukiwarka</li>	
		<li>Kontakt</li>
	</ul>

</div>

