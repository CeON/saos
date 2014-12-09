package pl.edu.icm.saos.api.services.representations.success.template;

/**
 * Represents maximum number of items on the page.
 * @author pavtel
 */
public class PageSizeTemplate extends QueryParameterRepresentation<Integer, String> {
    private static final long serialVersionUID = 770006168279778528L;

    //------------------------ CONSTRUCTORS --------------------------
    public PageSizeTemplate(Integer value) {
        super(value);
        setDescription("Represents maximum number of items on the page");
        setAllowedValues("Any positive integer");
    }
}
