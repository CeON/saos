<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="container" >

	<h3>Szukaj orzeczenia sądowego</h3>
	
	<form class="search form-horizontal" role="form">
	
		<div class="form-group">
			<label for="field_search" class="col-sm-2 control-label" >Szukana fraza</label>
			<div class="col-sm-8">
				<input name="field_search" type="text" />
			</div>
		</div>
	
		<div class="form-group">
			<label for="field_court" class="col-sm-2 control-label" >Sąd</label>
			<div class="col-sm-8">
				<input name="field_court" type="text" />
			</div>
		</div>
	
		<div class="form-group">
			<label for="field_judge" class="col-sm-2 control-label" >Sędzia</label>
				<div class="col-sm-8">
					<input name="field_judge" type="text" />
				</div>
		</div>
		
		
		<div class="form-group">
	        <div class="col-sm-offset-2 col-sm-10">
	        	<input class="btn btn-default" type="submit" value="Szukaj" />
	        </div>
	    </div>
	
	</form>

</div>







