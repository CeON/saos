package pl.edu.icm.saos.persistence.search.model;

/* Created on 2006-08-11 svnid: $Id: Order.java 5188 2007-05-14 19:15:28Z whury
 * $ */



import java.io.Serializable;

/**
 * Defines sorting of results. Results may be sorted with ascending or
 * descending order according to
 * <ul>
 * <li>relevance (hit accuracy),
 * <li>value of specified document's field.
 * </ul>
 *
 * @author wojtek
 * @author pavtel
 */
public final class Order implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The ascending. */
    private boolean ascending = true;

    /** The relevance. */
    private boolean relevance = false;

    /** The field. */
    private String field;

    /**
     * Instantiates a new order.
     */
    public Order() {
    }

    /**
     * Creates ascending order for given field.
     *
     * @param field the field
     */
    public Order(final String field) {
        this(field, true);
    }

    /**
     * Creates order for given field.
     *
     * @param field the field
     * @param ascending the ascending
     */
    public Order(final String field, final boolean ascending) {
        this.field = field;
        setAscending(ascending);
    }

    /**
     * Creates relevance order - results are sorted by search relavance (hit
     * accuracy).
     *
     * @return the order
     */
    public static Order relevanceOrder() {
        final Order ord = new Order();
        ord.relevance = true;
        ord.ascending = false;
        return ord;
    }

    /**
     * Returns <code>ascending</code> property of this order. Default order is
     * ascending.
     *
     * @return true, if is ascending
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * Sets the ascending.
     *
     * @param ascending the new ascending
     */
    public void setAscending(final boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * Returns name of the field which values should be sorted to determine
     * results order. Method returns <code>null</code> in case of relevance
     * order.
     *
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * Sets the field.
     *
     * @param field the new field
     * @throws SearchException the search exception
     */
    public void setField(final String field) throws SearchException {
        this.field = field;
        checkRelevanceAndField();
    }

    /**
     * Check relevance and field.
     *
     * @throws SearchException the search exception
     */
    private void checkRelevanceAndField() throws SearchException {
        if (relevance && field != null) {
            throw new SearchException("Invalid order - both field and relevance set (field=" + field + ").");
        }
    }

    /**
     * Returns true if this is relevance order.
     *
     * @return true, if is relevance
     */
    public boolean isRelevance() {
        return relevance;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (ascending ? 0 : 1);
        result = prime * result + ((field == null) ? 0 : field.hashCode());
        result = prime * result + (relevance ? 0 : 1);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (ascending != other.ascending) {
            return false;
        }
        if (field == null) {
            if (other.field != null) {
                return false;
            }
        } else if (!field.equals(other.field)) {
            return false;
        }
        if (relevance != other.relevance) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString() */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Order [ascending=").append(ascending).append(", relevance=").append(relevance)
                .append(", field=").append(field).append("]");
        return builder.toString();
    }

}
