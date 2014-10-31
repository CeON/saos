package pl.edu.icm.saos.api.single.judgment.data.representation;

import com.google.common.base.Objects;
import pl.edu.icm.saos.api.services.representations.success.Href;

import java.io.Serializable;
import java.util.List;

import static pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;

/**
 * Represents {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment} fields.
 * @author pavtel
 */
public class CommonCourtJudgmentData extends JudgmentData {

    private static final long serialVersionUID = 4320631178927257196L;

    protected Division division;
    protected List<String> keywords;

    //------------------------ GETTERS --------------------------

    public Division getDivision() {
        return division;
    }

    public List<String> getKeywords() {
        return keywords;
    }
    //------------------------ SETTERS --------------------------

    public void setDivision(Division division) {
        this.division = division;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(division, keywords);
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
        final CommonCourtJudgmentData other = (CommonCourtJudgmentData) obj;
        return Objects.equal(this.division, other.division) && Objects.equal(this.keywords, other.keywords);
    }


    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("href", href)
                .add("courtCases", courtCases)
                .add("judgmentType", judgmentType)
                .add("judgmentDate", judgmentDate)
                .add("source", source)
                .add("courtReporters", courtReporters)
                .add("decision", decision)
                .add("summary", summary)
                .add("textContent", textContent)
                .add("legalBases", legalBases)
                .add("referencedRegulations", referencedRegulations)
                .add("keywords", keywords)
                .add("division", division)
                .toString();
    }

    //------------------------ inner --------------------------

    public static class Division implements Serializable {
        private static final long serialVersionUID = -4459795985457420810L;

        private String name;
        private String href;
        private String code;
        private String type;
        private Court court;

        //------------------------ GETTERS --------------------------

        public String getCode() {
            return code;
        }

        public String getType() {
            return type;
        }

        public Court getCourt() {
            return court;
        }

        public String getName() {
            return name;
        }

        public String getHref() {
            return href;
        }

        //------------------------ SETTERS --------------------------

        public void setCode(String code) {
            this.code = code;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setCourt(Court court) {
            this.court = court;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setHref(String href) {
            this.href = href;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(name, href, code, type, court);
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
            return Objects.equal(this.name, other.name) &&
                    Objects.equal(this.href, other.href) &&
                    Objects.equal(this.code, other.code) &&
                    Objects.equal(this.type, other.type) &&
                    Objects.equal(this.court, other.court);
        }


        //------------------------ toString --------------------------


        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("name", name)
                    .add("href", href)
                    .add("code", code)
                    .add("type", type)
                    .add("court", court)
                    .toString();
        }
    }

    public static class Court implements Serializable {
        private static final long serialVersionUID = 6190992824642185703L;

        private String href;
        private String name;
        private String code;
        private CommonCourtType type;
        private Href parentCourt;

        //------------------------ GETTERS --------------------------

        public String getCode() {
            return code;
        }

        public CommonCourtType getType() {
            return type;
        }

        public Href getParentCourt() {
            return parentCourt;
        }

        public String getName() {
            return name;
        }

        public String getHref() {
            return href;
        }

        //------------------------ SETTERS --------------------------

        public void setCode(String code) {
            this.code = code;
        }

        public void setType(CommonCourtType type) {
            this.type = type;
        }

        public void setParentCourt(Href parentCourt) {
            this.parentCourt = parentCourt;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setHref(String href) {
            this.href = href;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(name, code, type, href, parentCourt);
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
            return Objects.equal(this.code, other.code) &&
                    Objects.equal(this.type, other.type) &&
                    Objects.equal(this.href, other.href) &&
                    Objects.equal(this.parentCourt, other.parentCourt) &&
                    Objects.equal(this.name, other.name)
                    ;
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("href", href)
                    .add("code", code)
                    .add("type", type)
                    .add("parentCourt", parentCourt)
                    .add("name", name)
                    .toString();
        }
    }

}
