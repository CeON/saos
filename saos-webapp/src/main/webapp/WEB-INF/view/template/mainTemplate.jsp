<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<!DOCTYPE html>
<html class="no-js" lang="<spring:message code="page.lang" />" >

    <c:set var="contextPath" value="${pageContext.request.contextPath}" scope="session"></c:set>  
    <c:set var="DATE_PATTERN" value="dd-MM-yyyy" scope="session"></c:set>  
    <c:set var="MONTH_YEAR_PATTERN" value="MM-yyyy" scope="session"></c:set>  
      
    <head>
	    <meta charset="UTF-8" />
	    <meta content="noindex, nofollow" name="robots">
	    
		<saos:cssJsInit></saos:cssJsInit>
		
		<%@ include file="/WEB-INF/view/common/pageTitle.jsp" %>
	    
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
        
        <%@ include file="piwik.jsp" %>
    
		<tiles:insertAttribute name="skipLinks" />

        <%@ include file="../common/cookiePolicy.jsp" %>

        <header>
            <tiles:insertAttribute name="header" />
        </header>
              
		<tiles:insertAttribute name="content" flush="false" />
        
        <footer class="footer" >
       		<tiles:insertAttribute name="footer" /> 
        </footer>
        
        <noscript>
	        <div class="container message-no-js">
	        	<spring:message code="message.noJavascript" />
	        </div>
        </noscript>
        
    </body>
</html>