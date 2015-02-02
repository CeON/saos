<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="path" required="true" description="Path for input text" rtexprvalue="true" %>
<%@ attribute name="labelName" required="true" description="Name of the label inside form field" rtexprvalue="true" %>
<%@ attribute name="labelText" required="true" description="Text describing label and text inside input placeholder" rtexprvalue="true" %>
<%@ attribute name="hintTitle" required="false" description="Hint title" rtexprvalue="true" %>
<%@ attribute name="hintContent" required="false" description="Hint content" rtexprvalue="true" %>

<c:set var="label" >
	<spring:message code="${labelText}" />
</c:set>

<div class="form-group">
	<label for="${labelName}" class="col-sm-2 control-label"><c:out value="${label}" />:</label>
   	<div class="col-sm-7">
    	<form:input path="${path}" class="form-control" id="${labelName}" placeholder="${label}" />
	</div>
	<c:if test="${!empty hintTitle && !empty hintContent}">
		<saos:hint title="${hintTitle}" content="#FOO" />
	</c:if>
</div>
