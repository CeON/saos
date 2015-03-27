<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="container page block" id="content" >
	<h3><spring:message code="header.siteMap" /></h3>
	
	<div class="site-map">
	
		<ul>
			<li>
				<a href="${contextPath}/" ><spring:message code="navigation.home" /></a>
			</li>
			<li>
				<a href="${contextPath}/search" ><spring:message code="navigation.search" /></a>
			</li>
            <li>
                <a href="${contextPath}/analysis" ><spring:message code="navigation.analysis" /></a>
            </li>
		</ul>
	
	</div>

</div>
