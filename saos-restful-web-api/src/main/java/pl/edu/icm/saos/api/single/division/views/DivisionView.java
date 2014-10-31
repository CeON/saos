package pl.edu.icm.saos.api.single.division.views;

import com.google.common.base.Objects;
import pl.edu.icm.saos.api.services.representations.success.SingleElementRepresentation;

import java.io.Serializable;

import static pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import static pl.edu.icm.saos.api.single.division.views.DivisionView.Data;

/**
 * @author pavtel
 */
public class DivisionView extends SingleElementRepresentation<Data>{

    private static final long serialVersionUID = -3626348086588845698L;

    public DivisionView() {
        data = new Data();
    }

    public static class Data implements Serializable {
        private static final long serialVersionUID = 4358135205423287209L;

        private String href;
        private String name;
        private String code;
        private String type;
        private Court court;

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

        public String getType() {
            return type;
        }

        public Court getCourt() {
            return court;
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

        public void setType(String type) {
            this.type = type;
        }

        public void setCourt(Court court) {
            this.court = court;
        }


        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(href, name, code, type, court);
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
            return Objects.equal(this.href, other.href) &&
                    Objects.equal(this.name, other.name) &&
                    Objects.equal(this.code, other.code) &&
                    Objects.equal(this.type, other.type) &&
                    Objects.equal(this.court, other.court);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("href", href)
                    .add("name", name)
                    .add("code", code)
                    .add("type", type)
                    .add("court", court)
                    .toString();
        }
    }

    public static class Court implements Serializable {

        private static final long serialVersionUID = -6974656408477763411L;

        private String href;
        private String name;
        private CommonCourtType type;
        private String code;
        private ParentCourt parentCourt;

        //------------------------ GETTERS --------------------------

        public String getHref() {
            return href;
        }

        public String getName() {
            return name;
        }

        public CommonCourtType getType() {
            return type;
        }

        public String getCode() {
            return code;
        }

        public ParentCourt getParentCourt() {
            return parentCourt;
        }

        //------------------------ SETTERS --------------------------

        public void setHref(String href) {
            this.href = href;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setType(CommonCourtType type) {
            this.type = type;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setParentCourt(ParentCourt parentCourt) {
            this.parentCourt = parentCourt;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(href, name, type, code);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Court other = (Court) obj;
            return Objects.equal(this.href, other.href) &&
                    Objects.equal(this.name, other.name) &&
                    Objects.equal(this.type, other.type) &&
                    Objects.equal(this.code, other.code) &&
                    Objects.equal(this.parentCourt, other.parentCourt);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("href", href)
                    .add("name", name)
                    .add("type", type)
                    .add("code", code)
                    .add("parentCode", parentCourt)
                    .toString();
        }
    }

    public static class ParentCourt implements  Serializable {

        private static final long serialVersionUID = 7030390092398444565L;

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
            final ParentCourt other = (ParentCourt) obj;
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
}
