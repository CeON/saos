package pl.edu.icm.saos.api.services.representations.success.template;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Represents general query parameter used as field in QUERY_TEMPLATE in the
 * {@link pl.edu.icm.saos.api.services.representations.success.CollectionRepresentation CollectionRepresentation}.
 * @author pavtel
 */
public class QueryParameterRepresentation<VALUE, ALLOWED_VALUES> implements Serializable{
    private static final long serialVersionUID = 1955198006971021383L;

    private VALUE value;
    private String description;
    private ALLOWED_VALUES allowedValues;

    //------------------------ CONSTRUCTORS --------------------------
    public QueryParameterRepresentation(VALUE value) {
        this.value = value;
    }

    //------------------------ GETTERS --------------------------
    public VALUE getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public ALLOWED_VALUES getAllowedValues() {
        return allowedValues;
    }

    //------------------------ SETTERS --------------------------

    public void setValue(VALUE value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAllowedValues(ALLOWED_VALUES allowedValues) {
        this.allowedValues = allowedValues;
    }

    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return Objects.hashCode(value, description, allowedValues);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final QueryParameterRepresentation other = (QueryParameterRepresentation) obj;
        return Objects.equal(this.value, other.value) &&
                Objects.equal(this.description, other.description) &&
                Objects.equal(this.allowedValues, other.allowedValues);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("value", value)
                .add("description", description)
                .add("allowedValues", allowedValues)
                .toString();
    }
}
