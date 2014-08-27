package pl.edu.icm.saos.search.config.model;

public enum JudgmentIndexField {
    ID("id"),
    DATABASE_ID("databaseId"),
    SIGNATURE("signature"),
    TITLE("title"),
    
    JUDGMENT_DATE("judgmentDate"),
    
    COURT_TYPE("courtType"),
    COURT_ID("courtId"),
    COURT_NAME("courtName"),
    COURT_DIVISION_ID("courtDivisionId"),
    COURT_DIVISION_NAME("courtDivisionName"),
    
    JUDGE("judge"),
    JUDGE_WITH_ROLE("judgeWithRole", true),
    
    KEYWORD("keyword"),
    CONTENT("content");
    
    private String fieldName;
    private boolean allowDynamicPostfix = false;
    
    private JudgmentIndexField(String fieldName) {
        this.fieldName = fieldName;
    }
    
    private JudgmentIndexField(String fieldName, boolean allowDynamicPostfix) {
        this.fieldName = fieldName;
        this.allowDynamicPostfix = allowDynamicPostfix;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public boolean isPostfixAllowed() {
        return allowDynamicPostfix;
    }
}
