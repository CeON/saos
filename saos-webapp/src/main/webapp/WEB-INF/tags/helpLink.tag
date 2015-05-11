<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="hint" description="Tag for displaying hints" %>

<%@ attribute name="title" required="true" description="Link title" rtexprvalue="true" %>
<%@ attribute name="pageLink" required="false" description="Url suffix to article on help page" rtexprvalue="true" %>


<a href='${contextPath}/help/${pageLink}' >
    ${title}
</a>

