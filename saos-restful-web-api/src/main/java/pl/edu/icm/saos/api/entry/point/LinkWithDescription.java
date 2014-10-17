package pl.edu.icm.saos.api.entry.point;

import java.io.Serializable;

/**
 * Represents link with description.
 * @author pavtel
 */
public class LinkWithDescription implements Serializable{

    private static final long serialVersionUID = -4632116104118258374L;

    private String rel;
    private String href;
    private String description;

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
