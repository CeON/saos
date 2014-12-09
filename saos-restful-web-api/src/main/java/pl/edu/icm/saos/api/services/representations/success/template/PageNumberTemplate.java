package pl.edu.icm.saos.api.services.representations.success.template;

/**
 * Represents current page number.
 * @author pavtel
 */
public class PageNumberTemplate extends QueryParameterRepresentation<Integer, String> {
    private static final long serialVersionUID = 3604308421324806214L;

    //------------------------ CONSTRUCTORS --------------------------
    public PageNumberTemplate(Integer value) {
        super(value);
        setDescription("Represents current page number");
        setAllowedValues("Not negative integer");
    }
}
