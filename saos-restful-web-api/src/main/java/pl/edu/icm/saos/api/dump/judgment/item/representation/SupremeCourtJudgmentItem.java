package pl.edu.icm.saos.api.dump.judgment.item.representation;

import java.io.Serializable;
import java.util.List;

import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;

import com.google.common.base.Objects;

/**
 * Represents item in items field in the {@link pl.edu.icm.saos.api.dump.judgment.views.DumpJudgmentsView DumpJudgmentsView}.
 * Contains data related to {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment SupremeCourtJudgment} specific fields.
 * @author pavtel
 */
public class SupremeCourtJudgmentItem extends JudgmentItem {
    private static final long serialVersionUID = 3511467718051492156L;

    private PersonnelType personnelType;
    private JudgmentForm judgmentForm;
    private Division division;
    private List<Chamber> chambers;


    //------------------------ GETTERS --------------------------

    public PersonnelType getPersonnelType() {
        return personnelType;
    }

    public JudgmentForm getJudgmentForm() {
        return judgmentForm;
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

    public void setJudgmentForm(JudgmentForm judgmentForm) {
        this.judgmentForm = judgmentForm;
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
        return 31 * super.hashCode() + Objects.hashCode(personnelType, judgmentForm, division, chambers);
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
                Objects.equal(this.judgmentForm, other.judgmentForm) &&
                Objects.equal(this.division, other.division) &&
                Objects.equal(this.chambers, other.chambers);
    }

    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("super", super.toString())
                .add("personnelType", getPersonnelType())
                .add("judgmentForm", getJudgmentForm())
                .add("division", getDivision())
                .add("chambers", getChambers())
                .toString();
    }
    
    
    public static class JudgmentForm implements Serializable {
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
            final JudgmentForm other = (JudgmentForm) obj;
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

        private long id;

        //------------------------ GETTERS --------------------------

        public long getId() {
            return id;
        }

        //------------------------ SETTERS --------------------------

        public void setId(long id) {
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

        private long id;

        //------------------------ GETTERS --------------------------


        public long getId() {
            return id;
        }

        //------------------------ SETTERS --------------------------

        public void setId(long id) {
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
