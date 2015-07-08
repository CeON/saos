<%@page pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="alert alert-danger">
    <spring:message code="error.wrongParamValue" arguments="${exception.paramName}"/>: <spring:message code="${exception.errorDetailsMessageCode}" arguments="${exception.errorDetailsMessageArgs}" />
</div>