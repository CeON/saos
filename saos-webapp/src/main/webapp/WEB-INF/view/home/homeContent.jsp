<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>



<div class="row block block-search">

    <a href="${contextPath}/search" aria-label='<spring:message code="home.navigation.search.desc" />'>    
        <div>
	        <img src="${contextPath}/static/image/home/search.png" aria-label='<spring:message code="home.navigation.search.imageAlt" /> - <spring:message code="home.navigation.search" />' alt='<spring:message code="home.navigation.search.imageAlt" />'/>
	        
	        <h2 class="box-title" >
	            <spring:message code="home.navigation.search" />
	        </h2>
	        
	        
            <p class="box-desc">
                <spring:message code="home.navigation.search.desc" />
            </p>
        
        </div>
    
    </a>     
</div>


<div class="row block block-analysis">
    <a href="${contextPath}/analysis" aria-label='<spring:message code="home.navigation.analysis.desc" />'>
    
        <div>
            <img src="${contextPath}/static/image/home/analysis.png" aria-label='<spring:message code="home.navigation.analysis.imageAlt" /> - <spring:message code="home.navigation.analysis" />' alt='<spring:message code="home.navigation.analysis.imageAlt" />'/>
        
	        <h2 class="box-title" >
	           <spring:message code="home.navigation.analysis" />
	        </h2>
	    
	        <p class="box-desc">
	           <spring:message code="home.navigation.analysis.desc" />
	        </p>
        </div>

    </a>    
</div>


<div class="row block block-api">
    <a href="${contextPath}/help/index.php/dokumentacja-api" aria-label='<spring:message code="home.navigation.api" /> - <spring:message code="home.navigation.api.desc" />'>
    
        <div>
            <img src="${contextPath}/static/image/home/api.png" aria-label='<spring:message code="home.navigation.api.imageAlt" /> - <spring:message code="home.navigation.api" />' alt='<spring:message code="home.navigation.api.imageAlt" />'/>
        
            <h2 class="box-title" >
                <spring:message code="home.navigation.api" />
            </h2>
        
            <p class="box-desc">
               <spring:message code="home.navigation.api.desc" />
            </p>
        </div>
        
    </a>    
</div>
                
                