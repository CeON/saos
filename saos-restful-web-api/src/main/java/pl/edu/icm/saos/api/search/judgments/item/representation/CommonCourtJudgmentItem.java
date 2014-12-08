package pl.edu.icm.saos.api.search.judgments.item.representation;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Represents single item in items field in the
 * {@link pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView SearchJudgmentsView}
 * Contains data related to {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment} specific fields.
 * @author pavtel
 */
public class CommonCourtJudgmentItem extends SearchJudgmentItem {

    private static final long serialVersionUID = -5527886583609335584L;

    protected Division division;

    //------------------------ GETTERS --------------------------

    public Division getDivision() {
        return division;
    }

    //------------------------ SETTERS --------------------------

    public void setDivision(Division division) {
        this.division = division;
    }

    //------------------------ HashCode & Equals --------------------------


    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(division);
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
        final CommonCourtJudgmentItem other = (CommonCourtJudgmentItem) obj;
        return Objects.equal(this.division, other.division);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("href", href)
                .add("courtCases", courtCases)
                .add("judgmentType", judgmentType)
                .add("JudgmentDate", JudgmentDate)
                .add("judges", judges)
                .add("textContent", textContent)
                .add("keywords", keywords)
                .add("division", division)
                .toString();
    }

    //------------------------ inner --------------------------

    public static class Division implements Serializable {
        private static final long serialVersionUID = -5553581160268600211L;

        private String href;
        private int id;
        private String name;
        private String code;
        private Court court;

        //------------------------ GETTERS --------------------------

        public String getHref() {
            return href;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public Court getCourt() {
            return court;
        }

        //------------------------ SETTERS --------------------------

        public void setHref(String href) {
            this.href = href;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setCourt(Court court) {
            this.court = court;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, href, name, code, court);
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
                    Objects.equal(this.code, other.code) &&
                    Objects.equal(this.court, other.court);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("href", href)
                    .add("name", name)
                    .add("code", code)
                    .add("court", court)
                    .toString();
        }
    }

    public static class Court implements Serializable {
        private static final long serialVersionUID = -1875340722763537635L;

        private String href;
        private int id;
        private String code;
        private String name;

        //------------------------ GETTERS --------------------------

        public String getHref() {
            return href;
        }

        public int getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        //------------------------ SETTERS --------------------------

        public void setHref(String href) {
            this.href = href;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setName(String name) {
            this.name = name;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, href, code, name);
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
            return Objects.equal(this.id, other.id) &&
                    Objects.equal(this.href, other.href) &&
                    Objects.equal(this.code, other.code) &&
                    Objects.equal(this.name, other.name);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("href", href)
                    .add("code", code)
                    .add("name", name)
                    .toString();
        }
    }


}
