<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>



<div class="row block block-search">

    <a href="${contextPath}/search" >    
        <div>
	        <img src="${contextPath}/static/image/home/search.png" alt="todo" />
	        
	        <p class="box-title" >
	            <spring:message code="home.navigation.search" />
	        </p>
	        
	        
            <p class="box-desc">
                <spring:message code="home.navigation.search.desc" />
            </p>
        
        </div>
    
    </a>     
</div>


<div class="row block block-analysis">
    <a href="${contextPath}/search" >
    
        <div>
            <img src="${contextPath}/static/image/home/analysis.png" alt="todo" />
        
	        <p class="box-title" >
	           <spring:message code="home.navigation.analysis" />
	        </p>
	    
	        <p class="box-desc">
	           <spring:message code="home.navigation.analysis.desc" />
	        </p>
        </div>

    </a>    
</div>


<div class="row block block-api">
    <a href="${contextPath}/search" >
    
        <div>
            <img src="${contextPath}/static/image/home/api.png" alt="todo" />
        
            <p class="box-title" >
                <spring:message code="home.navigation.api" />
            </p>
        
            <p class="box-desc">
               <spring:message code="home.navigation.api.desc" />
            </p>
        </div>
        
    </a>    
</div>
                
                