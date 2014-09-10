<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<div class="container search-form block">

	<h3><spring:message code="search.form.header" /></h3>

	<form class="form-horizontal" role="form">
	
	<fieldset id="search-fieldset" >
	
		<saos:formFieldText labelName="input-search-all" labelText="search.field.all" />
    
	    <ul>
	    	<li>
	    		<a id="search-more-fields" href="#" ><spring:message code="search.form.morefields" /></a>
    		</li>
	    </ul>
	    
	    <div id="advance-form">
	    
   			<saos:formFieldText labelName="input-search-casenumber" labelText="search.field.casenumber" />
		    
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
		    
		    <saos:formFieldText labelName="input-search-court" labelText="search.field.court" />

		    <saos:formFieldText labelName="input-search-division" labelText="search.field.division" />

		    
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
		    
		    <saos:formFieldText labelName="input-search-judge" labelText="search.field.judge" />
		    
		    <saos:formFieldText labelName="input-search-keywords" labelText="search.field.keywords" />
		    
		    <saos:formFieldText labelName="input-search-legalbases" labelText="search.field.legalbases" />
		     
		    <saos:formFieldText labelName="input-search-referencedregulations" labelText="search.field.referencedregulations" />
		     
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