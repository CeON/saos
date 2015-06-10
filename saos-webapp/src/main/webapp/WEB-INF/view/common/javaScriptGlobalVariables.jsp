<script>

var contextPath = '${pageContext.request.contextPath}', 
    DATE_PATTERN = '${DATE_PATTERN}'.toUpperCase(),

	/* Global variable that contains spring:message values
	 * used in javascript code. 
	 */
	springMessage = {
			judgmentSearchFormFieldDateWrongFormat: '<spring:message code="judgmentSearch.formField.dateWrongFormat" />',			
			judgmentSearchFormFieldKeywordsPlaceholder: '<spring:message code="judgmentSearch.formField.keywordsPlaceholder" />',
			judgmentSearchFormFieldLawJournalChoseItem: '<spring:message code="judgmentSearch.formField.lawJournal.choseItem" />',
			judgmentSearchFormFieldLawJournalNoItems: '<spring:message code="judgmentSearch.formField.lawJournal.noItems" />',
			judgmentSearchJudgmentSectionDefaultText: '<spring:message code="judgmentSearch.judgmentSection.defaultText" />',
		    judgmentSearchFilterAdd: '<spring:message code="judgmentSearch.filter.add" />',
		    judgmentSearchFilterSet: '<spring:message code="judgmentSearch.filter.set" />',
		    judgmentSearchFilterMessageBoxSet: '<spring:message code="judgmentSearch.filter.messageBox.set" />',
		 
			
			contextDateAnyValue: '<b><spring:message code="context.date.anyValue" /></b>',
			
			to: '<spring:message code="to" />',
			from: '<spring:message code="from" />',

			lang: '<spring:message code="page.lang" />',
			
			months: ['<spring:message code="month.1" />', '<spring:message code="month.2" />', '<spring:message code="month.3" />',
					'<spring:message code="month.4" />', '<spring:message code="month.5" />', '<spring:message code="month.6" />',
					'<spring:message code="month.7" />', '<spring:message code="month.8" />', '<spring:message code="month.9" />',
					'<spring:message code="month.10" />', '<spring:message code="month.11" />', '<spring:message code="month.12" />'],
					
		    monthsShort: ['<spring:message code="month.short.1" />', '<spring:message code="month.short.2" />', '<spring:message code="month.short.3" />',
             '<spring:message code="month.short.4" />', '<spring:message code="month.short.5" />', '<spring:message code="month.short.6" />',
             '<spring:message code="month.short.7" />', '<spring:message code="month.short.8" />', '<spring:message code="month.short.9" />',
             '<spring:message code="month.short.10" />', '<spring:message code="month.short.11" />', '<spring:message code="month.short.12" />']

	};

</script>
