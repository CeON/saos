package pl.edu.icm.saos.api.services.representations.success.template;

/**
 * Represents the latest of allowed judgment's date on the list of items
 * @author pavtel
 */
public class JudgmentDateToTemplate extends QueryParameterRepresentation<String, String> {
    private static final long serialVersionUID = -5504860961986070175L;

    public JudgmentDateToTemplate(String value) {
        super(value);
        setDescription("Represents the latest of allowed judgment's date on the list of items");
        setAllowedValues("Date in format : 'yyyy-MM-dd'");
    }
}
