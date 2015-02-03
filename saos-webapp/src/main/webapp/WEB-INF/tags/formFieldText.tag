<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="path" required="true" description="Path for input text" rtexprvalue="true" %>
<%@ attribute name="labelName" required="true" description="Name of the label inside form field" rtexprvalue="true" %>
<%@ attribute name="labelText" required="true" description="Text describing label" rtexprvalue="true" %>
<%@ attribute name="placeHolder" required="false" description="Text inside input placeholder" rtexprvalue="true" %>

<spring:message code="${labelText}" var="label" />

<c:if test="${!empty placeHolder}" >
	<spring:message code="${placeHolder}" var="placeHolder" />
</c:if>

<div class="form-group">
	<label for="${labelName}" class="col-sm-2 control-label"><c:out value="${label}" />:</label>
   	<div class="col-sm-7">
    	<form:input path="${path}" class="form-control" id="${labelName}" placeholder="${placeHolder}" />
	</div>
</div>
