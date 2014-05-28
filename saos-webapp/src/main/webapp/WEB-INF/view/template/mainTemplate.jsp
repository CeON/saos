<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>


<html class="no-js" >
    <c:set var="contextPath" value="${pageContext.request.contextPath}" scope="session"></c:set>  
    <c:set var="DATE_PATTERN" value="dd.MM.yyyy" scope="session"></c:set>  
      
    <head>
    <meta charset="UTF-8" />
    

    <%@include file="/WEB-INF/view/common/cssInit.jsp"%>
    <%@include file="/WEB-INF/view/common/javaScriptInit.jsp"%>
        
    <title></title>
    
    <script type="text/javascript">
        function clearSubmitMade() {
            __submitMade = false;
        }
        $(document).ready(function() {
        	
        	$.blockUI.defaults.css = { border: 'none', 
                    padding: '15px', 
                    backgroundColor: '#000', 
                    '-webkit-border-radius': '10px', 
                    '-moz-border-radius': '10px', 
                    opacity: .5, 
                    color: '#fff' };
        	$.blockUI.defaults.message = '<img src="${contextPath}/static/images/ajax-loader.gif">';
        	
            $.datepicker.setDefaults($.datepicker.regional["pl"]);
            $('[id^="datePicker_"]').datepicker({changeYear: 'true', yearRange: 'c-50:' + ((new Date()).getFullYear()+1) });
            
            $('[title]').tooltip({container: 'body'});
            
        });
    </script>
    
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
    
    <body onload="clearSubmitMade();">
       
        <header>
            <div class="container" >
	            <tiles:insertAttribute name="top" />
        	</div>
        </header>

		<nav class="menu" >
			<tiles:insertAttribute name="navigation" />
        </nav>
        
        <div class="container">        
	        <div class="row">   
                <tiles:insertAttribute name="content" flush="false" />
	        </div>
        </div>
        
       <tiles:insertAttribute name="footer" />
        
    </body>
</html>