package pl.edu.icm.saos.api.links.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import static pl.edu.icm.saos.api.ApiConstants.HREF;

/**
 * Represents view object in the form {'href': 'someUrl'}
 * @author pavtel
 */
public class Href {

    @JsonProperty(HREF)
    public String href;
}
