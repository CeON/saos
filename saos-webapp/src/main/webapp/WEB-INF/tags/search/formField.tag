<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="labelName" required="true" description="Law journal entry" rtexprvalue="true" %>
<%@ attribute name="labelText" required="true" description="Text inside link" rtexprvalue="true" %>

<div class="form-group">
	<label for="input-search-all" class="col-sm-2 control-label"><spring:message code="search.field.all" />:</label>
   	<div class="col-sm-7">
    	<input type="text" class="form-control" id="input-search-all" placeholder="<spring:message code="search.field.all" />">
	</div>
</div>