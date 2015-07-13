package pl.edu.icm.saos.api;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * @author pavtel
 */
public abstract class ApiConstants {

    public static final String COURT_CASES = "courtCases";
    public static final String CASE_NUMBER = "caseNumber";

    public static final String SELF = "self";
    public static final String NEXT = "next";
    public static final String PREV = "prev";
    public static final String HREF = "href";

    public static final String ID = "id";

    public static final String PAGE_SIZE = "pageSize";
    public static final String PAGE_NUMBER = "pageNumber";

    public static final String SORTING_FIELD = "sortingField";
    public static final String SORTING_DIRECTION = "sortingDirection";

    public static final String ALL = "all";

    public static final String CODE = "code";

    public static final String JUDGMENT_TYPE = "judgmentType";
    public static final String SOURCE = "source";
    public static final String JUDGMENT_URL = "judgmentUrl";
    public static final String JUDGMENT_ID = "judgmentId";
    public static final String PUBLISHER = "publisher";
    public static final String REVISER = "reviser";
    public static final String PUBLICATION_DATE = "publicationDate";
    public static final String JUDGMENT_DATE = "judgmentDate";

    public static final String JUDGES = "judges";
    public static final String NAME = "name";
    public static final String SPECIAL_ROLES = "specialRoles";

    public static final String COURT_REPORTERS = "courtReporters";
    public static final String DECISION = "decision";
    public static final String SUMMARY = "summary";
    public static final String TEXT_CONTENT = "textContent";
    public static final String PARENT_COURT = "parentCourt";

    public static final String TEXT = "text";


    public static final String LEGAL_BASES = "legalBases";
    public static final String REFERENCED_REGULATIONS = "referencedRegulations";
    public static final String JOURNAL_NO = "journalNo";
    public static final String JOURNAL_ENTRY = "journalEntry";
    public static final String JOURNAL_YEAR = "journalYear";
    public static final String JOURNAL_TITLE = "journalTitle";

    public static final String DIVISION = "division";
    public static final String DIVISIONS = "divisions";
    public static final String COURT = "court";
    public static final String TYPE = "type";
    public static final String KEYWORDS = "keywords";

    public static final String JUDGMENT_START_DATE = "judgmentStartDate";
    public static final String JUDGMENT_END_DATE = "judgmentEndDate";
    public static final String SINCE_MODIFICATION_DATE = "sinceModificationDate";
    public static final String WITH_GENERATED = "withGenerated";
    
    public static final String COURT_NAME = "courtName";
    public static final String LEGAL_BASE = "legalBase";
    public static final String REFERENCED_REGULATION = "referencedRegulation";
    public static final String LAW_JOURNAL_ENTRY_CODE = "lawJournalEntryCode";
    public static final String JUDGE_NAME = "judgeName";
    public static final String COURT_TYPE = "courtType";
    public static final String SC_PERSONNEL_TYPE = "scPersonnelType";
    public static final String SC_JUDGMENT_FORM = "scJudgmentForm";
    public static final String CC_COURT_TYPE = "ccCourtType";
    public static final String CC_COURT_ID = "ccCourtId";
    public static final String CC_COURT_CODE = "ccCourtCode";
    public static final String CC_COURT_NAME = "ccCourtName";
    public static final String CC_DIVISION_ID = "ccDivisionId";
    public static final String CC_DIVISION_CODE = "ccDivisionCode";
    public static final String CC_DIVISION_NAME = "ccDivisionName";
    public static final String CC_INCLUDE_DEPENDENT_COURT_JUDGMENTS = "ccIncludeDependentCourtJudgments";
    public static final String SC_CHAMBER_ID = "scChamberId";
    public static final String SC_CHAMBER_NAME = "scChamberName";
    public static final String SC_DIVISION_ID = "scDivisionId";
    public static final String SC_DIVISION_NAME = "scDivisionName";

    public static final String JUDGMENT_TYPES = "judgmentTypes";

    public static final String KEYWORD = "keyword";
    public static final String JUDGMENT_DATE_FROM = "judgmentDateFrom";
    public static final String JUDGMENT_DATE_TO = "judgmentDateTo";

    public static final String CHAMBER = "chamber";

    public static final String TOTAL_RESULTS = "totalResults";

    public static final JudgmentIndexField[] ALLOWED_SORTING_FIELDS = {
        JudgmentIndexField.DATABASE_ID,
        JudgmentIndexField.JUDGMENT_DATE,
        JudgmentIndexField.REFERENCING_JUDGMENTS_COUNT,
        JudgmentIndexField.MAXIMUM_MONEY_AMOUNT,
        JudgmentIndexField.CC_COURT_TYPE,
        JudgmentIndexField.CC_COURT_ID,
        JudgmentIndexField.CC_COURT_CODE,
        JudgmentIndexField.CC_COURT_NAME,
        JudgmentIndexField.CC_COURT_DIVISION_ID,
        JudgmentIndexField.CC_COURT_DIVISION_CODE,
        JudgmentIndexField.CC_COURT_DIVISION_NAME,
        JudgmentIndexField.SC_JUDGMENT_FORM_ID,
        JudgmentIndexField.SC_PERSONNEL_TYPE,
        JudgmentIndexField.SC_COURT_DIVISION_ID,
        JudgmentIndexField.SC_COURT_DIVISION_NAME,
        JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_ID,
        JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_NAME
    };
}