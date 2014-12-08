package pl.edu.icm.saos.api.single.scdivision.views;

import com.google.common.base.Objects;
import pl.edu.icm.saos.api.services.representations.success.SingleElementRepresentation;

import java.io.Serializable;

import static pl.edu.icm.saos.api.single.scdivision.views.DivisionView.Data;

/**
 * Represents {@link pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision SupremeCourtChamberDivision's} view.
 * @author pavtel
 */
public class DivisionView extends SingleElementRepresentation<Data>{
    private static final long serialVersionUID = 2341556628063111272L;

    public DivisionView() {
        setData(new Data());
    }

    //------------------------ INNER --------------------------

    public static class Data implements Serializable {
        private static final long serialVersionUID = 6102379234322603276L;

        private int id;
        private String href;
        private String name;
        private String fullName;
        private Chamber chamber;

        //------------------------ GETTERS --------------------------
        public String getName() {
            return name;
        }

        public String getFullName() {
            return fullName;
        }

        public Chamber getChamber() {
            return chamber;
        }

        public String getHref() {
            return href;
        }

        public int getId() {
            return id;
        }

        //------------------------ SETTERS --------------------------

        public void setName(String name) {
            this.name = name;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public void setChamber(Chamber chamber) {
            this.chamber = chamber;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void setId(int id) {
            this.id = id;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, href, name, fullName, chamber);
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
                    Objects.equal(this.fullName, other.fullName) &&
                    Objects.equal(this.chamber, other.chamber);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("href", href)
                    .add("name", name)
                    .add("fullName", fullName)
                    .add("chamber", chamber)
                    .toString();
        }
    }

    public static class Chamber implements Serializable {
        private static final long serialVersionUID = 1385060986281758552L;

        private int id;
        private String href;
        private String name;

        //------------------------ GETTERS --------------------------
        public String getHref() {
            return href;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        //------------------------ SETTERS --------------------------
        public void setHref(String href) {
            this.href = href;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(int id) {
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
            final Chamber other = (Chamber) obj;
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
