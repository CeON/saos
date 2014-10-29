package pl.edu.icm.saos.api.services.representations.success;

import com.google.common.base.Objects;
import org.springframework.hateoas.Link;

import java.io.Serializable;
import java.util.List;

/**
 * Represents success's view.
 * Contains common fields for view object.
 * @author pavtel
 */
public class SuccessRepresentation implements Serializable{

    private static final long serialVersionUID = 4450730190745381336L;

    protected List<Link> links;



    //------------------------ GETTERS --------------------------
    public List<Link> getLinks() {
        return links;
    }


    //------------------------ SETTERS --------------------------

    public void setLinks(List<Link> links) {
        this.links = links;
    }



    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return Objects.hashCode(links);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SuccessRepresentation other = (SuccessRepresentation) obj;
        return Objects.equal(this.links, other.links);
    }

    //------------------------ toString --------------------------
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("links", links)
                .toString();
    }
}
