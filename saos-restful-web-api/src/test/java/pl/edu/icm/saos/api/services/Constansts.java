package pl.edu.icm.saos.api.services;

/**
 * @author pavtel
 */
public class Constansts {

    public static final String JUDGMENTS_PATH = "/api/search/judgments";
    public static final String SINGLE_JUDGMENTS_PATH = "/api/judgments";
    public static final String DIVISIONS_PATH = "/api/divisions";
    public static final String COURTS_PATH = "/api/search/courts";
    public static final String SINGLE_COURTS_PATH = "/api/courts";
    public static final String DATE_FORMAT = "YYYY-MM-dd";

    public static final String JUDGMENT_PATH = SINGLE_JUDGMENTS_PATH+"/"+ FieldsDefinition.JC.JUDGMENT_ID;
    public static final String DIVISION_PATH = DIVISIONS_PATH+"/"+ FieldsDefinition.JC.DIVISION_ID;
    public static final String COURT_PATH = "/api/courts/"+ FieldsDefinition.JC.COURT_ID;
    public static final String PARENT_COURT_PATH = SINGLE_COURTS_PATH+"/"+ FieldsDefinition.JC.COURT_PARENT_ID;

    public static final String DUMP_JUDGMENTS_PATH = "/api/dump/judgments";
    public static final String DUMP_COURTS_PATH = "/api/dump/courts";
}
