package pl.edu.icm.saos.search.config.model;

/**
 * Fields of judgment index
 * 
 * @author madryk
 */
public enum JudgmentIndexField implements IndexField {
    ID("id"),
    DATABASE_ID("databaseId"),
    CASE_NUMBER("caseNumber"),
    
    JUDGMENT_DATE("judgmentDate"),
    JUDGMENT_TYPE("judgmentType"),
    LEGAL_BASE("legalBases"),
    REFERENCED_REGULATION("referencedRegulations"),
    
    COURT_TYPE("courtType"),
    
    /* common court */
    CC_COURT_TYPE("commonCourtType"),
    CC_COURT_ID("courtId"),
    CC_COURT_CODE("courtCode"),
    CC_COURT_NAME("courtName"),
    CC_COURT_DIVISION_ID("courtDivisionId"),
    CC_COURT_DIVISION_CODE("courtDivisionCode"),
    CC_COURT_DIVISION_NAME("courtDivisionName"),
    
    /* supreme court */
    SC_PERSONNEL_TYPE("personnelType"),
    SC_COURT_CHAMBER("courtChamber"),
    SC_COURT_CHAMBER_ID("courtChamberId"),
    SC_COURT_CHAMBER_NAME("courtChamberName"),
    SC_COURT_DIVISION_ID("courtChamberDivisionId"),
    SC_COURT_DIVISION_NAME("courtChamberDivisionName"),
    
    JUDGE("judge"),
    JUDGE_NAME("judgeName"),
    JUDGE_WITH_ROLE("judgeWithRole"),
    
    KEYWORD("keyword"),
    CONTENT("content");
    
    private String fieldName;
    
    
    private JudgmentIndexField(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public String getFieldName() {
        return fieldName;
    }
}
