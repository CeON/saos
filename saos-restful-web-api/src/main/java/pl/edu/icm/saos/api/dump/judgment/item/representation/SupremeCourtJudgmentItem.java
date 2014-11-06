package pl.edu.icm.saos.api.dump.judgment.item.representation;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;

import static pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;

/**
 * Represents item in items field in the {@link pl.edu.icm.saos.api.dump.judgment.views.DumpJudgmentsView DumpJudgmentsView}.
 * Contains data related to {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment SupremeCourtJudgment} specific fields.
 * @author pavtel
 */
public class SupremeCourtJudgmentItem extends JudgmentItem {
    private static final long serialVersionUID = 3511467718051492156L;

    protected PersonnelType personnelType;
    protected Form form;
    protected Division division;
    protected List<Chamber> chambers;

    public SupremeCourtJudgmentItem() {
        apiJudgmentType = ApiJudgmentType.SUPREME_COURT;
    }

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
        final SupremeCourtJudgmentItem other = (SupremeCourtJudgmentItem) obj;
        return Objects.equal(this.personnelType, other.personnelType) &&
                Objects.equal(this.form, other.form) &&
                Objects.equal(this.division, other.division) &&
                Objects.equal(this.chambers, other.chambers);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("courtCases", courtCases)
                .add("judgmentType", judgmentType)
                .add("JudgmentDate", JudgmentDate)
                .add("judges", judges)
                .add("source", source)
                .add("decision", decision)
                .add("courtReporters", courtReporters)
                .add("summary", summary)
                .add("textContent", textContent)
                .add("legalBases", legalBases)
                .add("referencedRegulations", referencedRegulations)
                .add("personnelType", personnelType)
                .add("form", form)
                .add("division", division)
                .add("chambers", chambers)
                .toString();
    }

    public static class Form implements Serializable {
        private static final long serialVersionUID = -1876029900371851361L;

        private String name;

        //------------------------ GETTERS --------------------------

        public String getName() {
            return name;
        }

        //------------------------ SETTERS --------------------------

        public void setName(String name) {
            this.name = name;
        }

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

    public static class Division implements Serializable {
        private static final long serialVersionUID = -8072032289200165385L;

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
            final Division other = (Division) obj;
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

    public static class Chamber implements Serializable {
        private static final long serialVersionUID = 2779757607973920820L;

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
            final Chamber other = (Chamber) obj;
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


}
