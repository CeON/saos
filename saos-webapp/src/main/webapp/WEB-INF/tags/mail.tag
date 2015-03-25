<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="mail" description="Tag for hiding e-mail adress" %>

<%@ attribute name="value" required="true" description="Email adress" rtexprvalue="true" %>

<c:set var="adress" value="${fn:replace(value, '@', '_AT_')}" />
<c:set var="adress" value="${fn:replace(adress, '.', '_DOT_')}" />

<a href="mailto:${adress}" class="dummy-mail" ><c:out value="${adress}" /></a>
