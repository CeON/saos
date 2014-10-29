package pl.edu.icm.saos.api.services.representations.success;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * @author pavtel
 */
public class Href implements Serializable{

    private static final long serialVersionUID = -4864322638220851133L;

    private String href;

    //------------------------ GETTERS --------------------------

    public String getHref() {
        return href;
    }

    //------------------------ SETTERS --------------------------

    public void setHref(String href) {
        this.href = href;
    }

    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return Objects.hashCode(href);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Href other = (Href) obj;
        return Objects.equal(this.href, other.href);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("href", href)
                .toString();
    }
}

