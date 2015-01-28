package pl.edu.icm.saos.api.single.court.views;

import java.io.Serializable;
import java.util.List;

import pl.edu.icm.saos.api.services.representations.success.SingleElementRepresentation;
import pl.edu.icm.saos.api.single.court.views.CommonCourtView.Data;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;

import com.google.common.base.Objects;

/**
 * Represents {@link pl.edu.icm.saos.persistence.model.CommonCourt CommonCourt} view.
 * @author pavtel
 */
public class CommonCourtView extends SingleElementRepresentation<Data>{
    private static final long serialVersionUID = -1345285608722368168L;

    public CommonCourtView() {
        setData(new Data());
    }

    public static class Data implements Serializable {

        private static final long serialVersionUID = -4574495269995004724L;

        private long id;
        private String href;
        private String name;
        private String code;
        private CommonCourtType type;
        private ParentCourt parentCourt;
        private List<Division> divisions;


        //------------------------ GETTERS --------------------------

        public String getHref() {
            return href;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
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

        public long getId() {
            return id;
        }

        //------------------------ SETTERS --------------------------

        public void setHref(String href) {
            this.href = href;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCode(String code) {
            this.code = code;
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

        public void setId(long id) {
            this.id = id;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, href, name, code, type, parentCourt, divisions);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Data other = (Data) obj;
            return Objects.equal(this.id, other.id) &&
                    Objects.equal(this.href, other.href) &&
                    Objects.equal(this.name, other.name) &&
                    Objects.equal(this.code, other.code) &&
                    Objects.equal(this.type, other.type) &&
                    Objects.equal(this.parentCourt, other.parentCourt) &&
                    Objects.equal(this.divisions, other.divisions);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("href", href)
                    .add("name", name)
                    .add("code", code)
                    .add("type", type)
                    .add("parentCourt", parentCourt)
                    .add("divisions", divisions)
                    .toString();
        }
    }

    public static class ParentCourt implements Serializable {

        private static final long serialVersionUID = 94472416729120415L;

        private long id;
        private String href;

        //------------------------ GETTERS --------------------------

        public String getHref() {
            return href;
        }

        public long getId() {
            return id;
        }

        //------------------------ SETTERS --------------------------

        public void setHref(String href) {
            this.href = href;
        }

        public void setId(long id) {
            this.id = id;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, href);
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
            return Objects.equal(this.id, other.id) &&
                    Objects.equal(this.href, other.href);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("href", href)
                    .toString();
        }
    }

    public static class Division implements Serializable {
        private static final long serialVersionUID = 3020882061218701135L;

        private long id;
        private String href;
        private String name;

        //------------------------ GETTERS --------------------------

        public String getHref() {
            return href;
        }

        public String getName() {
            return name;
        }

        public long getId() {
            return id;
        }

        //------------------------ SETTERS --------------------------

        public void setHref(String href) {
            this.href = href;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(long id) {
            this.id = id;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, href, name);
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
                    Objects.equal(this.href, other.href) &&
                    Objects.equal(this.name, other.name);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("href", href)
                    .add("name", name)
                    .toString();
        }
    }
}
