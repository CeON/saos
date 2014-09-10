<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="labelName" required="true" description="Name of the label inside form field" rtexprvalue="true" %>
<%@ attribute name="labelText" required="true" description="Text describing label and text inside input placeholder" rtexprvalue="true" %>

<div class="form-group">
	<label for="${labelName}" class="col-sm-2 control-label"><spring:message code="${labelText}" />:</label>
   	<div class="col-sm-7">
    	<input type="text" class="form-control" id="${labelName}" placeholder="<spring:message code="${labelText}" />">
	</div>
</div>
