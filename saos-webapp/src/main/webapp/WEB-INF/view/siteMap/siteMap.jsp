<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>



<div class="article container">

    <div class="row row-eq-height" >
	    
	    <div class="navigation col-xs-6">
	        
	        <%@ include file="../common/navigationMenu.jsp" %>
	        
	        <a href="${contextPath}/" class="saos-logo" title="" ></a>
	        
	    </div>
	
	    <div class="content col-xs-6">
	        
	        <h1><spring:message code="header.siteMap" /></h1>
	    
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
		            <li>
		                <a href="${contextPath}/help" ><spring:message code="navigation.help" /></a>
		            </li>
		        </ul>
		    
		    </div>
	        
	    </div>
	    
    </div>
    
</div>

