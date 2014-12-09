package pl.edu.icm.saos.api.services.representations.success.template;

/**
 * Represents the earliest allowed judgment's date on the list of items
 * @author pavtel
 */
public class JudgmentDateFromTemplate extends QueryParameterRepresentation<String, String> {
    private static final long serialVersionUID = -2949835045234643789L;

    //------------------------ CONSTRUCTORS --------------------------
    public JudgmentDateFromTemplate(String value) {
        super(value);
        setDescription("Represents the earliest allowed judgment's date on the list of items");
        setAllowedValues("Date in format : 'yyyy-MM-dd'");
    }
}
