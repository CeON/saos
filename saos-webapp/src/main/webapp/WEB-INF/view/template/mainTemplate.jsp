<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<!DOCTYPE html>

<html class="no-js" >
    <c:set var="contextPath" value="${pageContext.request.contextPath}" scope="session"></c:set>  
    <c:set var="DATE_PATTERN" value="dd-MM-yyyy" scope="session"></c:set>  
      
    <head>
    <meta charset="UTF-8" />
    
	<saos:cssJsInit></saos:cssJsInit>
    
        
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
	              
            <tiles:insertAttribute name="content" flush="false" />
	        
	        <div class="push"></div>
        </div>
        
        <footer>
       		<tiles:insertAttribute name="footer" /> 
        </footer>
        
    </body>
</html>