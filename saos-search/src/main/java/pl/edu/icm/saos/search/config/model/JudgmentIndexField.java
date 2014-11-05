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
    CC_TYPE("commonCourtType"),
    COURT_ID("courtId"),
    COURT_CODE("courtCode"),
    COURT_NAME("courtName"),
    COURT_DIVISION_ID("courtDivisionId"),
    COURT_DIVISION_CODE("courtDivisionCode"),
    COURT_DIVISION_NAME("courtDivisionName"),
    
    /* supreme court */
    SC_PERSONNEL_TYPE("personnelType"),
    SC_CHAMBER("courtChamber"),
    SC_CHAMBER_ID("courtChamberId"),
    SC_CHAMBER_NAME("courtChamberName"),
    SC_DIVISION_ID("courtChamberDivisionId"),
    SC_DIVISION_NAME("courtChamberDivisionName"),
    
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
