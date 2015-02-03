<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="enum" description="Prints localized enum" small-icon=""%>

<%@ attribute name="titleCode" required="true" description="Hint title" rtexprvalue="true" %>
<%@ attribute name="contentCode" required="true" description="Hint content" rtexprvalue="true" %>
 
<spring:message code="${titleCode}" var="title" />
<spring:message code="${contentCode}" var="content" />			

<a href="" class="hint" data-toggle="popover" data-trigger="focus" title="${title}" data-content="${content}">
	<spring:message code="judgmentSearch.hint.icon.alt" var="hintAltText" />
	<img src="${contextPath}/static/image/icons/info.png" height="30" alt="${hintAltText}" />
</a>
