<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div ng-controller="SearchController as sc">
	<div class="container search-form">
	
		<h3>Szukaj orzeczenia</h3>
	
	
		<form class="form-horizontal" role="form">
		  <div class="form-group">
		    <label for="inputEmail3" class="col-sm-2 control-label"></label>
		    <div class="col-sm-7">
		      <input data-ng-model="search" type="text" class="form-control" id="inputsearch" placeholder="wpisz fraze">
		    </div>
		  </div>
		  <div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		      <button type="submit" class="btn btn-primary button button-blue" ng-click="sc.getResults()" >Szukaj</button>
		    </div>
		  </div>
		</form>
		
	
	</div>
	
	<div class="container" >
		<h1>Wyniki</h1>
	
		<div  >
		
			<p>Wyniki wyszukiwania dla frazy: "{{sc.data.Heading}}"</p>
			
			<dl class="dl-horizontal" ng-repeat="item in sc.data.RelatedTopics.slice(0,3)" >
	            <dt>Text:</dt><dd>{{item.Text}}</dd>
	            <dt>Link:</dt><dd>{{item.Result}}</dd>
	        </dl>
			
		</div>
	
	</div>
</div>









