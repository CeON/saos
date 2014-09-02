<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<div class="container search-form block">

	<h3><spring:message code="search.form.header" /></h3>

	<form class="form-horizontal" role="form">
	
	<fieldset id="search-fieldset" >
	
		<div class="form-group">
			<label for="input-search-all" class="col-sm-2 control-label"><spring:message code="search.field.all" />:</label>
		    <div class="col-sm-7">
		      <input type="text" class="form-control" id="input-search-all" placeholder="<spring:message code="search.field.all" />">
		    </div>
	    </div>
	    
	    <ul>
	    	<li>
	    		<a id="search-more-fields" href="#" ><spring:message code="search.form.morefields" /></a>
    		</li>
	    </ul>
	    
	    <div id="advance-form">
	    
	    	<div class="form-group">
			    <label for="input-search-casenumber" class="col-sm-2 control-label"><spring:message code="search.field.casenumber" />:</label>
			    <div class="col-sm-4">
			      <input type="text" class="form-control" id="input-search-casenumber" placeholder="<spring:message code="search.field.casenumber" />">
			    </div>
			    
			    
		    </div>
		    
		    <div class="form-group">
			    <label for="datepicker_from" class="col-sm-2 control-label"><spring:message code="search.field.date" />:</label>
			    <div class="col-sm-2">
			      <input type="date" class="form-control" id="datepicker_from" placeholder="<spring:message code="search.field.date.from" />" >
			    </div>
			    <label for="datepicker_to" class="col-sm-1 control-label"></label>
			    <div class="col-sm-2">
			      <input type="date" class="form-control" id="datepicker_to" placeholder="<spring:message code="search.field.date.to" />" >
			    </div>
		    </div>
		    
		    <div class="form-group">
   		    	<label for="input-search-court" class="col-sm-2 control-label"><spring:message code="search.field.court" />:</label>
			    <div class="col-sm-7">
			      <input type="text" class="form-control" id="input-search-court" placeholder="<spring:message code="search.field.court" />">
			    </div>
			    
		    </div>
		    
		    <div class="form-group">
   		    	<label for="input-search-court" class="col-sm-2 control-label"><spring:message code="search.field.division" />:</label>
			    <div class="col-sm-7">
			      <input type="text" class="form-control" id="input-search-court" placeholder="<spring:message code="search.field.division" />">
			    </div>
			    
		    </div>
		    
		    <div class="form-group">
		    	<label for="input-search-all" class="col-sm-2 control-label"><spring:message code="search.field.judgmenttype" />:</label>
   			    <div class="col-sm-6">
			      <div class="checkbox">
		    	      <input type="checkbox" value="" >
		    	      <label><spring:message code="search.judgmenttype.decision" /></label>
		        	  <input type="checkbox" value="" >
		        	  <label><spring:message code="search.judgmenttype.resolution" /></label>
         	          <input type="checkbox" value="" >
         	          <label><spring:message code="search.judgmenttype.sentence" /></label>
			      </div>
			    </div>
		    </div>
		    
		    <div class="form-group">
			    <label for="input-search-judge" class="col-sm-2 control-label"><spring:message code="search.field.judge" />:</label>
			    <div class="col-sm-7">
			      <input type="text" class="form-control" id="input-search-judge" placeholder="<spring:message code="search.field.judge" />">
			    </div>
		    </div>
		    
		    <div class="form-group">
			    <label for="input-search-keywords" class="col-sm-2 control-label"><spring:message code="search.field.keywords" />:</label>
			    <div class="col-sm-7">
			      <input type="text" class="form-control" id="input-search-keywords" placeholder="<spring:message code="search.field.keywords" />">
			    </div>
		    </div>
		    
		    <div class="form-group">
			    <label for="input-search-legalbases" class="col-sm-2 control-label"><spring:message code="search.field.legalbases" />:</label>
			    <div class="col-sm-7">
			      <input type="text" class="form-control" id="input-search-legalbases" placeholder="<spring:message code="search.field.legalbases" />">
			    </div>
		    </div>
		     
		     <div class="form-group">
			    <label for="input-search-referencedregulations" class="col-sm-2 control-label"><spring:message code="search.field.referencedregulations" />:</label>
			    <div class="col-sm-7">
			      <input type="text" class="form-control" id="input-search-referencedregulations" placeholder="<spring:message code="search.field.referencedregulations" />">
			    </div> 
		    </div>
	    </div>
	
		<ul class="display-none" >
	    	<li>
	    		<a id="search-less-fields" href="#" ><spring:message code="search.form.lessfields" /></a>
    		</li>
	    </ul>
	
		<div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		      <button type="submit" class="btn btn-primary button button-blue"><spring:message code="search.button.search" /></button>
		    </div>
   		</div>
	
	
	</fieldset>
	
	
    
	
	  
	</form>
</div>