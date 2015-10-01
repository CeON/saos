<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
        

        
<div class="analysis container">

    <div class="row row-eq-height" >
        <div class="navigation col-md-4">
            
            <%@ include file="../common/navigationMenu.jsp" %>
            
            <%@ include file="../common/saosLogo.jsp" %>
	           
            <div id="analysis" tabindex="-1">
                <div class="form-group" id="analysisFormDiv">
            
                    <%@ include file="analysisForm.jsp" %>
                    
                </div>
            </div>
        </div>
    
		<div class="content col-md-8" id="content" tabindex="-1">
            
            <%@ include file="analysisContent.jsp" %>
            
        </div>
    </div>

</div>
        
        
        