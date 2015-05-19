<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="lawJournalEntry" required="true" description="Law journal entry" rtexprvalue="true" type="pl.edu.icm.saos.persistence.model.LawJournalEntry" %>
<%@ attribute name="text" required="true" description="Text inside link" rtexprvalue="true" %>
<%@ attribute name="tooltipText" required="true" description="Text in link tooltip" rtexprvalue="true" %>

<spring:message code="judgmentDetails.lawJournal.link.alt" var="externalLawJournalLinkAlt" />

<div class="lawJournal-link">
	<a href="${contextPath}/search?lawJournalEntryCode=${lawJournalEntry.entryCode}" data-toggle="tooltip" title="${tooltipText}" >
		<c:out value="${text}" />
	</a>
	
	<a href="http://dziennikustaw.gov.pl/du/${lawJournalEntry.year}/s/${lawJournalEntry.journalNo}/${lawJournalEntry.entry}" >
		<img src="${contextPath}/static/image/icons/external_small.png" height="10" alt="${externalLawJournalLinkAlt}" />
	</a>
</div>


