package pl.edu.icm.saos.api.utils;

/**
 * @author pavtel
 */
public class Constansts {

    public static final String JUDGMENTS_PATH = "/api/judgments";
    public static final String DIVISIONS_PATH = "/api/divisions";
    public static final String COURTS_PATH = "/api/courts";
    public static final String DATE_FORMAT = "YYYY-MM-dd";

    public static final String JUDGMENT_PATH = JUDGMENTS_PATH+"/"+ FieldsDefinition.JC.JUDGMENT_ID;
    public static final String DIVISION_PATH = DIVISIONS_PATH+"/"+ FieldsDefinition.JC.DIVISION_ID;
    public static final String COURT_PATH = COURTS_PATH+"/"+ FieldsDefinition.JC.COURT_ID;
    public static final String PARENT_COURT_PATH = COURTS_PATH+"/"+ FieldsDefinition.JC.COURT_PARENT_ID;
}
