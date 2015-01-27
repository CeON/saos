package pl.edu.icm.saos.api.dump.supreme.court.chamber.views;

import java.io.Serializable;
import java.util.List;

import pl.edu.icm.saos.api.dump.supreme.court.chamber.views.DumpScChambersView.Info;
import pl.edu.icm.saos.api.dump.supreme.court.chamber.views.DumpScChambersView.Item;
import pl.edu.icm.saos.api.dump.supreme.court.chamber.views.DumpScChambersView.QueryTemplate;
import pl.edu.icm.saos.api.services.representations.success.CollectionRepresentation;
import pl.edu.icm.saos.api.services.representations.success.template.PageNumberTemplate;
import pl.edu.icm.saos.api.services.representations.success.template.PageSizeTemplate;

import com.google.common.base.Objects;

/**
 * Represents dump supreme court chambers view
 * @author pavtel
 */
public class DumpScChambersView extends CollectionRepresentation<Item, QueryTemplate, Info> {
    private static final long serialVersionUID = -3553679368331481200L;

    public static class Item implements Serializable {
        private static final long serialVersionUID = -7485850054670753236L;

        private long id;
        private String name;
        private List<Division> divisions;

        //------------------------ GETTERS --------------------------


        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<Division> getDivisions() {
            return divisions;
        }

        //------------------------ SETTERS --------------------------

        public void setId(long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDivisions(List<Division> divisions) {
            this.divisions = divisions;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, name, divisions);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Item other = (Item) obj;
            return Objects.equal(this.id, other.id) &&
                    Objects.equal(this.name, other.name) &&
                    Objects.equal(this.divisions, other.divisions);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("name", name)
                    .add("divisions", divisions)
                    .toString();
        }
    }

    public static class Division implements Serializable {
        private static final long serialVersionUID = 5371366723251759736L;

        private long id;
        private String name;
        private String fullName;

        //------------------------ GETTERS --------------------------

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getFullName() {
            return fullName;
        }

        //------------------------ SETTERS --------------------------

        public void setId(long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, name, fullName);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Division other = (Division) obj;
            return Objects.equal(this.id, other.id) &&
                    Objects.equal(this.name, other.name) &&
                    Objects.equal(this.fullName, other.fullName);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("name", name)
                    .add("fullName", fullName)
                    .toString();
        }
    }

    public static class QueryTemplate implements Serializable {
        private static final long serialVersionUID = -9175231935730751891L;

        private PageSizeTemplate pageSize;
        private PageNumberTemplate pageNumber;

        //------------------------ GETTERS --------------------------

        public PageSizeTemplate getPageSize() {
            return pageSize;
        }

        public PageNumberTemplate getPageNumber() {
            return pageNumber;
        }


        //------------------------ SETTERS --------------------------

        public void setPageSize(PageSizeTemplate pageSize) {
            this.pageSize = pageSize;
        }

        public void setPageNumber(PageNumberTemplate pageNumber) {
            this.pageNumber = pageNumber;
        }


        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(pageSize, pageNumber);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final QueryTemplate other = (QueryTemplate) obj;
            return Objects.equal(this.pageSize, other.pageSize) &&
                    Objects.equal(this.pageNumber, other.pageNumber);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("pageSize", pageSize)
                    .add("pageNumber", pageNumber)
                    .toString();
        }
    }

    public static class Info implements Serializable {
        private static final long serialVersionUID = -2503882898367888472L;
    }
}
