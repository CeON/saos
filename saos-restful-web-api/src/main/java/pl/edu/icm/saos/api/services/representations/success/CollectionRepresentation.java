package pl.edu.icm.saos.api.services.representations.success;

import com.google.common.base.Objects;

import java.util.List;

/**
 * Represents collections view.
 * @author pavtel
 */
public class CollectionRepresentation<ITEMS, QUERY_TEMPLATE, INFO>  extends SuccessRepresentation {

    private static final long serialVersionUID = -7666954281326403905L;

    protected List<ITEMS> items;

    protected QUERY_TEMPLATE queryTemplate;

    protected INFO info;

    //------------------------ GETTERS --------------------------

    public List<ITEMS> getItems() {
        return items;
    }

    public QUERY_TEMPLATE getQueryTemplate() {
        return queryTemplate;
    }

    public INFO getInfo() {
        return info;
    }

    //------------------------ SETTERS --------------------------

    public void setItems(List<ITEMS> items) {
        this.items = items;
    }

    public void setQueryTemplate(QUERY_TEMPLATE queryTemplate) {
        this.queryTemplate = queryTemplate;
    }

    public void setInfo(INFO info) {
        this.info = info;
    }

    //------------------------ HashCode & Equals --------------------------


    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(items, queryTemplate, info);
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
        final CollectionRepresentation other = (CollectionRepresentation) obj;
        return Objects.equal(this.items, other.items) && Objects.equal(this.queryTemplate, other.queryTemplate) && Objects.equal(this.info, other.info);
    }

    //------------------------ toString --------------------------


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("links", links)
                .add("items", items)
                .add("queryTemplate", queryTemplate)
                .add("info", info)
                .toString();
    }
}
