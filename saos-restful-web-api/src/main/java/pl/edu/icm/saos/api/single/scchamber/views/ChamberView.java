package pl.edu.icm.saos.api.single.scchamber.views;

import com.google.common.base.Objects;
import pl.edu.icm.saos.api.services.representations.success.SingleElementRepresentation;

import java.io.Serializable;
import java.util.List;
import static pl.edu.icm.saos.api.single.scchamber.views.ChamberView.Data;

/**
 * Represents {@link pl.edu.icm.saos.persistence.model.SupremeCourtChamber SupremeCourtChamber's} view.
 * @author pavtel
 */
public class ChamberView extends SingleElementRepresentation<Data>{

    private static final long serialVersionUID = 5060953088977952916L;

    public ChamberView() {
        setData(new Data());
    }

    public static class Data implements Serializable {
        private static final long serialVersionUID = -7592137519490410182L;

        private int id;
        private String href;
        private String name;
        private List<Division> divisions;

        //------------------------ GETTERS --------------------------

        public String getHref() {
            return href;
        }

        public String getName() {
            return name;
        }

        public List<Division> getDivisions() {
            return divisions;
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

        public void setDivisions(List<Division> divisions) {
            this.divisions = divisions;
        }

        public void setId(int id) {
            this.id = id;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, href, name, divisions);
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
                    Objects.equal(this.divisions, other.divisions);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("href", href)
                    .add("name", name)
                    .add("divisions", divisions)
                    .toString();
        }
    }

    public static class Division implements Serializable {
        private static final long serialVersionUID = -8868206176270508350L;

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
