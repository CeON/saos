<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<div class="spinner" >

	<div class="container">
		<div class="stage"> 
			<div class="block-info" >
			
				<h3>
				<spring:message code="carousel.search.header" />  
				</h3>
				
				<p>
				Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident,
				</p>
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
	<!-- 
	<img src="/static/image/gavel.jpg" >
	 -->


</div>


<div class="container advert-idea">

	<div class="" >
		<h2 class="advert-header" ><spring:message code="home.header.idea" /></h2>
	
		<div class="item-desc" >
			<h3><spring:message code="saos.welcome.header" /></h3>
	 		<p><spring:message code="saos.welcome.text" /></p>
		</div>
	</div>
</div>


<div class="advert-ground">
	<div class="container">
	
		<div class="row">
			<div class="col-md-4 item">
				<div class="block search"></div>
				<p class="item-title">
					<spring:message code="home.navigation.search" />
				</p>
				<p class="item-desc">
				At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti
				</p>
			</div>
			<div class="col-md-4 item">
				<div class="block stats"></div>
				<p class="item-title">
					<spring:message code="home.navigation.stats" />
				</p>
				<p class="item-desc">
				At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti
				</p>
			</div>
			<div class="col-md-4 item">
				<div class="block api"></div>
				<p class="item-title">
					<spring:message code="home.navigation.api" />
				</p>
				<p class="item-desc">
				At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti
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
		<div class="col-md-4 "><img src="${contextPath}/static/image/footer/icm_logo.png" /></div>
		<div class="col-md-4 "><%-- <img src="${contextPath}/static/image/footer/opi_logo.jpg" /> --%></div>
	</div>
</div>

