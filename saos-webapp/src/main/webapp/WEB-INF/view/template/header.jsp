<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="container" >
	<h1><spring:message code="saos.fullname"/></h1>
</div>

<div role="navigation" class="navbar navbar-default" id="nav-menu">
  <div class="container">
    <div class="navbar-collapse collapse">
      <ul class="nav navbar-nav" id="navigation-links" >
      	<li class="active"><a href="${contextPath}/"><spring:message code="navigation.home" /></a></li>
        <li ><a href="${contextPath}/search"><spring:message code="navigation.search" /></a></li>
        <li ><a href="${contextPath}/analysis"><spring:message code="navigation.analysis" /></a></li>
      </ul>      
    </div>
  </div>
</div>


