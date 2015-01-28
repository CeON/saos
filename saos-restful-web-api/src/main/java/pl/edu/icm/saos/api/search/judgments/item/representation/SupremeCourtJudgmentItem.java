package pl.edu.icm.saos.api.search.judgments.item.representation;

import java.io.Serializable;
import java.util.List;

import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;

import com.google.common.base.Objects;

/**
 *  Represents single item in items field in the
 * {@link pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView SearchJudgmentsView}
 * Contains data related to {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment SupremeCourtJudgment} specific fields.
 * @author pavtel
 */
public class SupremeCourtJudgmentItem extends SearchJudgmentItem {
    private static final long serialVersionUID = -2267420542588216734L;

    private PersonnelType personnelType;
    private Division division;

    //------------------------ GETTERS --------------------------

    public PersonnelType getPersonnelType() {
        return personnelType;
    }

    public Division getDivision() {
        return division;
    }

    //------------------------ SETTERS --------------------------

    public void setPersonnelType(PersonnelType personnelType) {
        this.personnelType = personnelType;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    //------------------------ HashCode & Equals --------------------------
    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(personnelType, division);
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
        final SupremeCourtJudgmentItem other = (SupremeCourtJudgmentItem) obj;
        return Objects.equal(this.personnelType, other.personnelType) &&
                Objects.equal(this.division, other.division);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(super.toString())
                .add("personnelType", personnelType)
                .add("division", division)
                .toString();
    }

    //------------------------ INNER --------------------------
    public static class Division implements Serializable {
        private static final long serialVersionUID = -8801751085086840339L;

        private String href;
        private long id;
        private String name;
        private List<Chamber> chambers;


        //------------------------ GETTERS --------------------------

        public String getHref() {
            return href;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<Chamber> getChambers() {
            return chambers;
        }

        //------------------------ SETTERS --------------------------

        public void setHref(String href) {
            this.href = href;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setChambers(List<Chamber> chambers) {
            this.chambers = chambers;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, href, name, chambers);
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
                    Objects.equal(this.name, other.name) &&
                    Objects.equal(this.chambers, other.chambers);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("href", href)
                    .add("name", name)
                    .add("chambers", chambers)
                    .toString();
        }
    }

    public static class Chamber implements Serializable {
        private static final long serialVersionUID = -5507770923252323958L;

        private String href;
        private long id;
        private String name;


        //------------------------ GETTERS --------------------------

        public String getHref() {
            return href;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        //------------------------ SETTERS --------------------------

        public void setHref(String href) {
            this.href = href;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
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
