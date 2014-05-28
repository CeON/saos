<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<div class="top-navigation" >
	<div class="horizontal-right" >
		<!-- 
		<div class='language-change' >
			<img title="" src="${contextPath}/static/images/icons/pl_flag.png" />
			<img title="" src="${contextPath}/static/images/icons/en_flag.png" />
		</div>
		 -->
		 <security:authorize access="isAuthenticated()">
		      <span class="badge" style="margin-right: 5px;"><span><spring:message code="header.loggedAs"/>:</span> <security:authentication property="name"/></span><a href="${contextPath}/logout"><button class="btn btn-mini btn-secondary" ><spring:message code="button.logout"/></button></a>
		</security:authorize>
		<security:authorize access="!isAuthenticated()">
              <a href="${contextPath}/login"><button class="btn btn-mini btn-secondary" ><spring:message code="button.login"/></button></a>
        </security:authorize>
	</div>
</div>
<nav class="left" >
    <ul class="horizontal-nav">
        <li><a target="_blank" href="/help">POMOC</a></li>
	</ul>
</nav>
<h1><spring:message code="saos.fullname"/></h1>