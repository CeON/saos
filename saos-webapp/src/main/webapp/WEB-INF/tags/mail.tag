<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="mail" description="Tag for hiding e-mail address" %>

<%@ attribute name="value" required="true" description="Email address" rtexprvalue="true" %>

<c:set var="address" value="${fn:replace(value, '@', '_AT_')}" />
<c:set var="address" value="${fn:replace(address, '.', '_DOT_')}" />

<a href="mailto:${address}" class="dummy-mail" ><c:out value="${address}" /></a>
