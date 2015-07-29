<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%-- Below, search box in google search results --%>
<script type="application/ld+json">
{
  "@context": "http://schema.org",
  "@type": "WebSite",
  "url": "${homePageUrl}",
  "potentialAction": {
    "@type": "SearchAction",
    "target": "${homePageUrl}search?all={search_term_string}",
    "query-input": "required name=search_term_string"
  }
}
</script>
        
        
<div class="home container">

    <div class="row row-eq-height">
    
	    <div class="navigation col-xs-6">
	        
	        <%@ include file="../common/navigationMenu.jsp" %>
	        
	        <%@ include file="../home/homeNavigation.jsp" %>
	        
	    </div>
	
	    <div class="content col-xs-6" id="content">
	        <%@ include file="../home/homeContent.jsp" %>
	        
	    </div>
	    
	</div>
	    
		<div class="partners container">
			
		    <div class="col-md-2 col-xs-6">
		        <a href="http://www.ncbir.pl/ ">
		            <img height="70" src="${contextPath}/static/image/footer/ncbir_logo.jpg" alt="<spring:message code="partners.ncbir.imageAlt" />" />
		        </a>
		    </div>
		    <div class="col-md-2 col-xs-6">
		        <a href="http://www.icm.edu.pl">
		            <img height="70" src="${contextPath}/static/image/footer/icm_logo.jpg" alt="<spring:message code="partners.icm.imageAlt" />" />
		        </a>
		    </div>
		    <div class="col-md-2 col-xs-6">
		        <a href="http://www.ncbir.pl/programy-krajowe/innowacje-spoleczne/">
		            <img height="70" src="${contextPath}/static/image/footer/logo_is.jpg" alt="<spring:message code="partners.is.imageAlt" />" />
		        </a>
		    </div>
		    <div class="col-md-2 col-xs-6">
		        <a href="http://www.akceslab.pl">
		            <img height="70" src="${contextPath}/static/image/footer/logoakceslab.png" alt="<spring:message code="partners.akceslab.imageAlt" />" />
		        </a>
		    </div>
		    <div class="col-md-2 col-xs-6">
		        <a href="http://siecobywatelska.pl/">
		            <img height="45" style="margin-top:15px" src="${contextPath}/static/image/footer/so-logo.png" alt="<spring:message code="partners.sowp.imageAlt" />" />
		        </a>
		    </div>
		    <div class="col-md-2 col-xs-6">
		        <a href="http://ofop.eu/">
		            <img height="70" src="${contextPath}/static/image/footer/ofop_logo.png" alt="<spring:message code="partners.ofop.imageAlt" />" />
		        </a>
		    </div>
		</div>
	
        <div class="project-info col-xs-12">
        	<spring:message code="footer.project.info"/>
        </div>
        <div class="app-info-footer">
            <spring:message code="appInfo.contact"/>: <spring:eval var="contactMailAddress" expression="@exposedProperties.getProperty('contact.generalMailAddress')"/>
            <saos:mail value="${contactMailAddress}"/> 
            | 
            <spring:message code="appInfo.saosVersion"/>: <spring:eval expression="@versionProperties.getProperty('saos.version')"/>
        </div>
    

</div>

        