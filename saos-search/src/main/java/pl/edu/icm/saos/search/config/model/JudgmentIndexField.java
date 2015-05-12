package pl.edu.icm.saos.search.config.model;

/**
 * Fields of judgment index
 * 
 * @author madryk
 */
public enum JudgmentIndexField implements IndexField {
    DATABASE_ID("databaseId"),
    CASE_NUMBER("caseNumber"),
    
    JUDGMENT_DATE("judgmentDate"),
    JUDGMENT_TYPE("judgmentType"),
    LEGAL_BASE("legalBases"),
    REFERENCED_REGULATION("referencedRegulations"),
    LAW_JOURNAL_ENTRY_ID("lawJournalEntryId"),
    REFERENCED_COURT_CASE_ID("referencedCourtCasesIds"),
    REFERENCING_JUDGMENTS_COUNT("referencingJudgmentsCount"),
    MAXIMUM_MONEY_AMOUNT("maximumMoneyAmount"),
    
    SOURCE_CODE("sourceCode"),
    COURT_TYPE("courtType"),
    
    /* common court */
    CC_COURT_TYPE("ccCourtType"),
    CC_COURT_ID("ccCourtId"),
    CC_COURT_CODE("ccCourtCode"),
    CC_COURT_NAME("ccCourtName"),
    CC_COURT_DIVISION_ID("ccCourtDivisionId"),
    CC_COURT_DIVISION_CODE("ccCourtDivisionCode"),
    CC_COURT_DIVISION_NAME("ccCourtDivisionName"),

    CC_APPEAL_COURT_ID("ccAppealCourtId"),
    CC_APPEAL_NAME("ccAppealName"),
    
    CC_REGIONAL_COURT_ID("ccRegionalCourtId"),
    CC_REGION_NAME("ccRegionName"),
    
    CC_DISTRICT_COURT_ID("ccDistrictCourtId"),
    CC_DISTRICT_NAME("ccDistrictName"),
    
    /* supreme court */
    SC_JUDGMENT_FORM_ID("scJudgmentFormId"),
    SC_JUDGMENT_FORM_NAME("scJudgmentFormName"),
    SC_PERSONNEL_TYPE("scPersonnelType"),
    SC_COURT_CHAMBER("scCourtChamber"),
    SC_COURT_CHAMBER_ID("scCourtChamberId"),
    SC_COURT_CHAMBER_NAME("scCourtChamberName"),
    SC_COURT_DIVISION_ID("scCourtChamberDivisionId"),
    SC_COURT_DIVISION_NAME("scCourtChamberDivisionName"),
    SC_COURT_DIVISIONS_CHAMBER_ID("scCourtDivisionsChamberId"),
    SC_COURT_DIVISIONS_CHAMBER_NAME("scCourtDivisionsChamberName"),
    
    /* constitutional tribunal */
    CT_DISSENTING_OPINION("ctDissentingOpinion"),
    CT_DISSENTING_OPINION_AUTHOR("ctDissentingOpinionAuthor"),
    
    JUDGE("judge"),
    JUDGE_NAME("judgeName"),
    JUDGE_WITH_ROLE("judgeWithRole"),
    
    KEYWORD("keyword"),
    CONTENT("content"),
    ALL("all");
    
    private String fieldName;
    
    
    private JudgmentIndexField(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public static boolean hasFieldName(String property) {
    	boolean propertyExists = false;
    	
    	for (JudgmentIndexField judgmentIndexField : JudgmentIndexField.values()) {
    		if (judgmentIndexField.name().equals(property)) {
    			propertyExists = true;
    		}
    	}
    	
    	return propertyExists;
    }
}
