<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="hint" description="Tag for displaying hints" %>

<%@ attribute name="title" required="true" description="Hint title" rtexprvalue="true" %>
<%@ attribute name="content" required="true" description="Hint content" rtexprvalue="true" %>
 		
<spring:message code="judgmentSearch.hint.icon.alt" var="hintAltText" />

<span class="hint" tabindex="0" data-html="true" data-trigger="focus" data-toggle="popover" title="${title}" data-content="${content}">
	<img src="${contextPath}/static/image/icons/info.png" height="26" alt="${hintAltText}" />
</span>
