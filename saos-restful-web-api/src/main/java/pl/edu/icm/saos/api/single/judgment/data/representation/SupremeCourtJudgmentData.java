package pl.edu.icm.saos.api.single.judgment.data.representation;

import java.io.Serializable;
import java.util.List;

import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;

import com.google.common.base.Objects;

/**
 * Represents {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment SupremeCourtJudgment} fields.
 * @author pavtel
 */
public class SupremeCourtJudgmentData extends JudgmentData {
    private static final long serialVersionUID = 9192584970769834239L;

    protected PersonnelType personnelType;
    protected Form form;
    protected Division division;
    protected List<Chamber> chambers;

    //------------------------ GETTERS --------------------------
    public PersonnelType getPersonnelType() {
        return personnelType;
    }

    public Form getForm() {
        return form;
    }

    public Division getDivision() {
        return division;
    }

    public List<Chamber> getChambers() {
        return chambers;
    }

    //------------------------ SETTERS --------------------------
    public void setPersonnelType(PersonnelType personnelType) {
        this.personnelType = personnelType;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public void setChambers(List<Chamber> chambers) {
        this.chambers = chambers;
    }

    //------------------------ HashCode & Equals --------------------------


    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(personnelType, form, division, chambers);
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
        final SupremeCourtJudgmentData other = (SupremeCourtJudgmentData) obj;
        return Objects.equal(this.personnelType, other.personnelType) &&
                Objects.equal(this.form, other.form) &&
                Objects.equal(this.division, other.division) &&
                Objects.equal(this.chambers, other.chambers);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("super", super.toString())
                .add("personnelType", personnelType)
                .add("form", form)
                .add("division", division)
                .add("chambers", chambers)
                .toString();
    }

    public static class Form implements Serializable {
        private static final long serialVersionUID = 7521132117768045711L;

        private String name;

        //------------------------ GETTERS --------------------------
        public String getName() {
            return name;
        }

        //------------------------ SETTERS --------------------------
        public void setName(String name) {
            this.name = name;
        }

        //------------------------ HashCode & Equals --------------------------
        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Form other = (Form) obj;
            return Objects.equal(this.name, other.name);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("name", name)
                    .toString();
        }
    }

    public static class Division implements Serializable{
        private static final long serialVersionUID = -4521773159723379249L;

        private String href;
        private String name;
        private Chamber chamber;

        //------------------------ GETTERS --------------------------

        public String getHref() {
            return href;
        }

        public String getName() {
            return name;
        }

        public Chamber getChamber() {
            return chamber;
        }

        //------------------------ SETTERS --------------------------

        public void setHref(String href) {
            this.href = href;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setChamber(Chamber chamber) {
            this.chamber = chamber;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(href, name, chamber);
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
            return Objects.equal(this.href, other.href) &&
                    Objects.equal(this.name, other.name) &&
                    Objects.equal(this.chamber, other.chamber);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("href", href)
                    .add("name", name)
                    .add("chamber", chamber)
                    .toString();
        }
    }

    public static class Chamber implements Serializable{
        private static final long serialVersionUID = 7877817346120144973L;

        private String href;
        private String name;

        //------------------------ GETTERS --------------------------

        public String getHref() {
            return href;
        }

        public String getName() {
            return name;
        }

        //------------------------ SETTERS --------------------------

        public void setHref(String href) {
            this.href = href;
        }

        public void setName(String name) {
            this.name = name;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(href, name);
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
            return Objects.equal(this.href, other.href) &&
                    Objects.equal(this.name, other.name);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("href", href)
                    .add("name", name)
                    .toString();
        }
    }


}
