<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="year" required="true" description="Law journal year" rtexprvalue="true" %>
<%@ attribute name="journalNo" required="true" description="Law journal number" rtexprvalue="true" %>
<%@ attribute name="entry" required="true" description="Law journal entry" rtexprvalue="true" %>
<%@ attribute name="text" required="true" description="Text inside link" rtexprvalue="true" %>

<a href="http://dziennikustaw.gov.pl/${year}/s/${journalNo}/${entry}" >
	<c:out value="${text}" />
</a>




