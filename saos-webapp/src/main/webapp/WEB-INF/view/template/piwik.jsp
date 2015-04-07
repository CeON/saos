<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<spring:eval var="piwikSiteId" expression="@exposedProperties.getProperty('piwik.siteId')"/>  
   <c:if test="${piwikSiteId != 0}">
        <!-- Piwik -->
        <script type="text/javascript">
              var _paq = _paq || [];
              _paq.push(['trackPageView']);
              _paq.push(['enableLinkTracking']);
              (function() {
                var u="//piwik.vls.icm.edu.pl/";
                _paq.push(['setTrackerUrl', u+'piwik.php']);
                _paq.push(['setSiteId', ${piwikSiteId}]);
                var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
                g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
             })();
        </script>
        <noscript><p><img src="//piwik.vls.icm.edu.pl/piwik.php?idsite=${piwikSiteId}" style="border:0;" alt="" /></p></noscript>
        <!-- End Piwik Code -->
   
   </c:if>