<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<!DOCTYPE html>

<html class="no-js" ng-app="search" >
    <c:set var="contextPath" value="${pageContext.request.contextPath}" scope="session"></c:set>  
    <c:set var="DATE_PATTERN" value="dd.MM.yyyy" scope="session"></c:set>  
      
    <head>
    <meta charset="UTF-8" />
    
	<link href='http://fonts.googleapis.com/css?family=Noto+Sans:400,400italic,700,700italic&subset=latin,latin-ext,cyrillic-ext' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,700,700italic&subset=latin,latin-ext,cyrillic-ext' rel='stylesheet' type='text/css'>
	<link href='http://fonts.googleapis.com/css?family=Lato:400,300,300italic,700,400italic' rel='stylesheet' type='text/css'>
	<link type="text/css"  rel="stylesheet" href="${contextPath}/static/stylesheet/css/bootstrap.min.css"/>    
	<link type="text/css"  rel="stylesheet" href="${contextPath}/static/stylesheet/.generated/modern.css"/>      
	<link type="text/css"  rel="stylesheet" href="${contextPath}/static/stylesheet/css/smoothness/jquery-ui-1.10.4.custom.min.css"/>
    
    <%@include file="/WEB-INF/view/common/javaScriptInitDev.jsp"%>
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.min.js"></script>
	<script type="text/javascript" src="${contextPath}/static/javascript/angular/app.js"></script>
        
    <title><spring:message code="saos.fullname"/></title>

    
    <%-- have to overwrite the font-face declaration because firefox does not work with fontface relative urls --%>
    <c:set var="glyphiconsFontsUrl" value="${contextPath}/static/font/bootstrap" scope="page"/>
    <style type="text/css">
        @font-face {
        font-family: 'Glyphicons Halflings';
        src: url("${glyphiconsFontsUrl}/glyphicons-halflings-regular.eot");
        src: url("${glyphiconsFontsUrl}/glyphicons-halflings-regular.eot?#iefix") format("embedded-opentype"), url("${glyphiconsFontsUrl}/glyphicons-halflings-regular.woff") format("woff"), url("${glyphiconsFontsUrl}/glyphicons-halflings-regular.ttf") format("truetype"), url("${glyphiconsFontsUrl}/glyphicons-halflings-regular.svg#glyphicons_halflingsregular") format("svg");
        }
    </style>

    </head>
    
    <body>
       	<div class="wrapper" >
	        <header>
	            <tiles:insertAttribute name="top" />
	        </header>
	
			<nav class="menu" >
				<tiles:insertAttribute name="navigation" />
	        </nav>
	        
            <tiles:insertAttribute name="content" flush="false" />
	        
	        <div class="push"></div>
        </div>
        <footer>
       		<tiles:insertAttribute name="footer" /> 
        </footer>
       
    </body>
</html>