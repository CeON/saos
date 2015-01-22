<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<div role="navigation" class="navbar navbar-blank navbar-static-top">
	<div class="container">
		<ul class="nav navbar-nav navbar-right language-change">
        	<li><a href="#">EN</a></li>
      	</ul>
		<ul class="nav navbar-nav navbar-right language-change">
        	<li><a href="#"><span class="font-small" >A</span><span class="font-big" >A</span></a></li>
      	</ul>
	</div>
</div>

<div class="container" >

	<div class="top-navigation" >
		<div class="horizontal-right" >
			<!-- 
			<div class='language-change' >
				<img title="" src="${contextPath}/static/images/icons/pl_flag.png" />
				<img title="" src="${contextPath}/static/images/icons/en_flag.png" />
			</div>
			 -->
			 <security:authorize access="isAuthenticated()">
			 	<!-- 
			      <span class="badge" style="margin-right: 5px;"><span><spring:message code="header.loggedAs"/>:</span> <security:authentication property="name"/></span><a href="${contextPath}/logout"><button class="btn btn-mini btn-secondary" ><spring:message code="button.logout"/></button></a>
			       -->
			</security:authorize>
			<security:authorize access="!isAuthenticated()">
			 	<!-- 
	              <a href="${contextPath}/login"><button class="btn btn-mini btn-secondary" ><spring:message code="button.login"/></button></a>
	              -->
	        </security:authorize>
		</div>
	</div>
	
	<h1><spring:message code="saos.fullname"/></h1>

</div>

<div role="navigation" class="navbar navbar-default" id="nav-menu">
  <div class="container">
    <div class="navbar-header">
      <button data-target=".navbar-collapse" data-toggle="collapse" class="navbar-toggle" type="button">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
    </div>
    <div class="navbar-collapse collapse">
      <ul class="nav navbar-nav">
      	<li class="active"><a href="${contextPath}/"><spring:message code="header.navigation.home" /></a></li>
        <li ><a href="${contextPath}/search"><spring:message code="header.navigation.search" /></a></li>
        <li><a href="#"><spring:message code="header.navigation.contact" /></a></li>
      </ul>
      
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#"><spring:message code="header.navigation.login" /></a></li>
      </ul>
      
    </div><!--/.nav-collapse -->
  </div><!--/.container-fluid -->
</div>


