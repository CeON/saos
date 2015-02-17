<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%--
<div class="spinner" id="content" >

	<div class="container">
		<div class="stage"> 
			<div class="block-info" >
				<h3><spring:message code="home.carousel.searchHeader" /></h3>
				<p><spring:message code="home.carousel.searchDescription" /></p>
			</div>
		</div>
		<div class="pagination">
			<ul>
				<li class="active" ></li>
				<li></li>
				<li></li>
			</ul>
		</div>
		
	</div>
</div>
 --%>

<div class="container advert-idea">

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
				</a>
				<p class="item-desc">
					<spring:message code="lorem.text30" />
				</p>
			</div>
			<div class="col-md-4 item">
				<a href="#" class="item-title">
					<div class="block stats"></div>
					<spring:message code="home.navigation.stats" />
				</a>
				<p class="item-desc">
					<spring:message code="lorem.text30" />
				</p>
			</div>
			<div class="col-md-4 item">
				<a href="#" class="item-title">
					<div class="block api"></div>
					<spring:message code="home.navigation.api" />
				</a>
				<p class="item-desc">
					<spring:message code="lorem.text30" />
				</p>
			</div>
		</div>
	
	</div>
</div>

<div class="container advert-stats" >
	  

</div>



<div class="container advert-partners" >
	<h2 class="advert-header" ><spring:message code="home.header.partners" /></h2>

	<div class="advert-frame container">
		<%--<div class="col-md-4 "><img src="${contextPath}/static/image/footer/humanCapital_pl.png" /></div>
		<div class="col-md-4 "><img src="${contextPath}/static/image/footer/ue_logo.jpg" /></div>
		<div class="col-md-4 "><img src="${contextPath}/static/image/footer/ic_logo.jpg" /></div> --%>
		<div class="col-md-4 "><%-- <img src="${contextPath}/static/image/footer/mnisw_logo.jpg" /> --%></div>
		<div class="col-md-4 "><a href="http://www.icm.edu.pl/ "><img src="${contextPath}/static/image/footer/icm_logo.png" alt="<spring:message code="partners.icm.imageAlt" />" /></a></div>
		<div class="col-md-4 "><%-- <img src="${contextPath}/static/image/footer/opi_logo.jpg" /> --%></div>
	</div>
</div>

