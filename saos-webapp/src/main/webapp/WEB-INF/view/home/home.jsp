<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="container" >
	<div class="beta-info row">
		<div class="col-sm-1">
			<img height="64" src="${contextPath}/static/image/icons/warning.png" alt="" />
		</div>
		<div class="col-sm-10" >
			<p>
				<spring:message code="home.helpdeskAddress" var="helpdeskAddress" />
				
				<spring:message code="home.beta.message" />
				<saos:mail value="${helpdeskAddress}" />.
			</d>
		</div>
	</div>
</div>

<div id="content" class="container advert-idea">

	<div class="" >
		<h2 class="advert-header" ><spring:message code="home.header.idea" /></h2>
	
		<div class="item-desc" >
			<h3><spring:message code="home.welcome.header" /></h3>
	 		<p><spring:message code="home.welcome.text" /></p>
		</div>
	</div>
</div>


<div class="advert-ground">
	<div class="container">
	
		<div class="row">
			<div class="col-md-4 item">
				<a href="${contextPath}/search" class="item-title">
					<div class="block search"></div>
					<spring:message code="home.navigation.search" />
					<p class="item-desc">
						<spring:message code="home.navigation.search.desc" />
					</p>
				</a>
			</div>
			<div class="col-md-4 item">
				<a href="${contextPath}/analysis" class="item-title">
					<div class="block stats"></div>
					<spring:message code="home.navigation.analysis" />
					<p class="item-desc">
						<spring:message code="home.navigation.analysis.desc" />
					</p>
				</a>
			</div>
			<div class="col-md-4 item">
				<a href="${contextPath}/api" class="item-title">
					<div class="block api"></div>
					<spring:message code="home.navigation.api" />
					<p class="item-desc">
						<spring:message code="home.navigation.api.desc" />
					</p>
				</a>
			</div>
		</div>
	
	</div>
</div>

<div class="container advert-stats" >
	  

</div>



<div class="container advert-partners" >
	<h2 class="advert-header" ><spring:message code="home.header.partners" /></h2>

	<div class="advert-frame container">
		<div class="col-md-3">
			<a href="http://www.icm.edu.pl/ ">
				<img height="60" src="${contextPath}/static/image/footer/icm_logo.png" alt="<spring:message code="partners.icm.imageAlt" />" />
			</a>
		</div>
		<div class="col-md-3 ">
			<a href="http://www.akceslab.pl">
				<img height="70" src="${contextPath}/static/image/footer/logoakceslab.png" alt="<spring:message code="partners.akceslab.imageAlt" />" />
			</a>
		</div>
		<div class="col-md-3 ">
			<a href="http://siecobywatelska.pl/">
				<img height="70" src="${contextPath}/static/image/footer/so-logo.png" alt="<spring:message code="partners.sowp.imageAlt" />" />
			</a>
		</div>
		<div class="col-md-3 ">
			<a href="http://ofop.eu/">
				<img height="70" src="${contextPath}/static/image/footer/ofop_logo.png"  alt="<spring:message code="partners.ofop.imageAlt" />" />
			</a>
		</div>
	</div>
</div>

