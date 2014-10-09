<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<div class="form-group">
	<label for="" class="col-sm-2 control-label"><spring:message code="search.field.personneltype" />:</label>
   	<div class="col-sm-7">
    	<select class="form-control" id="personnel-type" >
    		<option></option>
    		<saos:enumOptions enumType="pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType" prefix="judgment.personneltype" />
    	</select>
	</div>
</div>