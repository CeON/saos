package pl.edu.icm.saos.api.links.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import static pl.edu.icm.saos.api.ApiConstants.HREF;

/**
 * @author pavtel
 */
public class Href {

    @JsonProperty(HREF)
    public String href;
}
