package pl.edu.icm.saos.api.services.representations.success;

import com.google.common.base.Objects;

/**
 * Represents single element view.
 * @author pavtel
 */
public class SingleElementRepresentation<DATA> extends SuccessRepresentation {

    private static final long serialVersionUID = -1601095442499004941L;

    protected DATA data;

    //------------------------ GETTERS --------------------------

    public DATA getData() {
        return data;
    }

    //------------------------ SETTERS --------------------------

    public void setData(DATA data) {
        this.data = data;
    }

    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final SingleElementRepresentation other = (SingleElementRepresentation) obj;
        return Objects.equal(this.data, other.data);
    }

    //------------------------ toString --------------------------


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("links", links)
                .add("data", data)
                .toString();
    }
}
