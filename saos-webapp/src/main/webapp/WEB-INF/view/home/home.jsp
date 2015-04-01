<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="container" >
	<div class="beta-info row">   
		<div class="col-sm-12" >
			<p>
				<spring:eval expression="@exposedProperties.getProperty('webapp.helpdeskAddress')" var="helpdeskAddress" />
				
				<spring:message code="home.beta.message" />
				<saos:mail value="${helpdeskAddress}" />.
			</p>
        </div>
    </div>
</div>

<div id="content" class="container home-content">
	
	<div class="col-sm-12">
	   <p><spring:message code="home.welcome.text" /></p>
	</div>
	
	
    <div class="col-sm-12 boxes">
	    <div class="col-sm-4" >
	       <a href="${contextPath}/search" >
	           <div class="box">
		           <div class="block search"></div>
			       
			       <p class="box-title" >
			           <spring:message code="home.navigation.search" />
			       </p>
			       <p>
			           <spring:message code="home.navigation.search.desc" />
			       </p>
	           </div>
	       </a>
		</div>
		<div class="col-sm-4" >
		    <a href="${contextPath}/search" >
			   <div class="box">
				    <div class="block stats"></div>
				    <p class="box-title" >
			           <spring:message code="home.navigation.analysis" />
			        </p>
			        <p>
			           <spring:message code="home.navigation.analysis.desc" />
			        </p>
		        </div>
		    </a>
	    </div>
	    <div class="col-sm-4" >
	       <a href="${contextPath}/search" >
		       <div class="box">
			       <div class="block api"></div>
			       <p class="box-title" >
			          <spring:message code="home.navigation.api" />
			       </p>
			       <p>
			           <spring:message code="home.navigation.api.desc" />
			       </p>
		       </div>
	       </a>
	    </div>
    </div>
	

</div>


<div class="container partners" >

	<div class="frame container">
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

