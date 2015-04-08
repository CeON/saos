<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="container" >
	<h1><spring:message code="saos.fullname"/></h1>
</div>

<div role="navigation" class="navbar navbar-default" id="nav-menu">
  <div class="container">
    <div class="navbar-header">
	    <button aria-controls="navbar" aria-expanded="false" data-target="#navbar" data-toggle="collapse" class="navbar-toggle collapsed" type="button">
		    <span class="sr-only">Toggle navigation</span>
		    <span class="icon-bar"></span>
		    <span class="icon-bar"></span>
		    <span class="icon-bar"></span>
	    </button>
    </div>
    <div class="navbar-collapse collapse" id="navbar">
      <ul class="nav navbar-nav " id="navigation-links" >
      	<li ><a href="${contextPath}/"><spring:message code="navigation.home" /></a></li>
        <li ><a href="${contextPath}/search"><spring:message code="navigation.search" /></a></li>
        <li ><a href="${contextPath}/analysis"><spring:message code="navigation.analysis" /></a></li>
      </ul>      
    </div>
  </div>
</div>


