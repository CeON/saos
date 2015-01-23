<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="skip-links row">
	<ul class="col-md-4 col-md-offset-4" >
		<li>
			<a href="#nav-menu" >
				<spring:message code="skipLinks.nav" />
			</a>
		</li>

		<tiles:insertAttribute name="skipLinksContent" />

		<li>
			<a href="${contextPath}/siteMap" >
				<spring:message code="skipLinks.siteMap" />
			</a>
		</li>
	</ul>
</div>
