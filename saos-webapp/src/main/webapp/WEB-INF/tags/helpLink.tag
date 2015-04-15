<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="hint" description="Tag for displaying hints" %>

<%@ attribute name="title" required="true" description="Link title" rtexprvalue="true" %>
<%@ attribute name="pageLink" required="false" description="Url suffix to article on help page" rtexprvalue="true" %>

<spring:eval expression="@exposedProperties.getProperty('webapp.helpAddress')" var="helpPageAddress" />

<c:set var="address" value="${helpPageAddress}${pageLink}" />

<a href='${address}' >
    ${title}
</a>

