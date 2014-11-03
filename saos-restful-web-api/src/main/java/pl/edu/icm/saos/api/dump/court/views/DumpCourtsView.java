package pl.edu.icm.saos.api.dump.court.views;

import com.google.common.base.Objects;
import pl.edu.icm.saos.api.services.representations.success.CollectionRepresentation;

import java.io.Serializable;
import java.util.List;

import static pl.edu.icm.saos.api.dump.court.views.DumpCourtsView.*;
import static pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;

/**
 * Represents dump court's view.
 * @author pavtel
 */
public class DumpCourtsView extends CollectionRepresentation<Item, QueryTemplate, Info>{

    private static final long serialVersionUID = -708083153744057397L;

    public static class Item implements Serializable {
        private static final long serialVersionUID = -8050957944518505399L;

        private int id;
        private String name;
        private CommonCourtType type;
        private String code;
        private ParentCourt parentCourt;
        private List<Division> divisions;

        //------------------------ GETTERS --------------------------


        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public CommonCourtType getType() {
            return type;
        }

        public ParentCourt getParentCourt() {
            return parentCourt;
        }

        public List<Division> getDivisions() {
            return divisions;
        }

        public String getCode() {
            return code;
        }

        //------------------------ SETTERS --------------------------

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setType(CommonCourtType type) {
            this.type = type;
        }

        public void setParentCourt(ParentCourt parentCourt) {
            this.parentCourt = parentCourt;
        }

        public void setDivisions(List<Division> divisions) {
            this.divisions = divisions;
        }

        public void setCode(String code) {
            this.code = code;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, name, type, code, parentCourt, divisions);
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
                    Objects.equal(this.type, other.type) &&
                    Objects.equal(this.code, other.code) &&
                    Objects.equal(this.parentCourt, other.parentCourt) &&
                    Objects.equal(this.divisions, other.divisions)
                    ;
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("name", name)
                    .add("type", type)
                    .add("code", code)
                    .add("parentCourt", parentCourt)
                    .add("divisions", divisions)
                    .toString();
        }
    }

    public static class ParentCourt implements Serializable {
        private static final long serialVersionUID = 3621337286069280544L;

        private int id;

        //------------------------ GETTERS --------------------------

        public int getId() {
            return id;
        }

        //------------------------ SETTERS --------------------------

        public void setId(int id) {
            this.id = id;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final ParentCourt other = (ParentCourt) obj;
            return Objects.equal(this.id, other.id);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .toString();
        }
    }

    public static class Division implements Serializable {
        private static final long serialVersionUID = 9034101309332487890L;

        private int id;
        private String name;
        private String code;
        private String type;

        //------------------------ GETTERS --------------------------

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public String getType() {
            return type;
        }

        //------------------------ SETTERS --------------------------

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setType(String type) {
            this.type = type;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, name, code, type);
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
                    Objects.equal(this.code, other.code) &&
                    Objects.equal(this.type, other.type);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("name", name)
                    .add("code", code)
                    .add("type", type)
                    .toString();
        }
    }

    public static class QueryTemplate implements Serializable {
        private static final long serialVersionUID = -9175231935730751891L;

        private int pageSize;
        private int pageNumber;

        //------------------------ GETTERS --------------------------

        public int getPageSize() {
            return pageSize;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        //------------------------ SETTERS --------------------------

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setPageNumber(int pageNumber) {
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
