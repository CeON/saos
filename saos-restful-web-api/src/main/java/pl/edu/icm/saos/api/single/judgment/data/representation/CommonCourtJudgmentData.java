package pl.edu.icm.saos.api.single.judgment.data.representation;

import java.io.Serializable;

import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;

import com.google.common.base.Objects;

/**
 * Represents {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment} fields.
 * @author pavtel
 */
public class CommonCourtJudgmentData extends JudgmentData {

    private static final long serialVersionUID = 4320631178927257196L;

    private Division division;
    
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
        final CommonCourtJudgmentData other = (CommonCourtJudgmentData) obj;
        return Objects.equal(this.division, other.division);
    }


    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("super", super.toString())
                .add("division", division)
                .toString();
    }

    //------------------------ inner --------------------------

    public static class Division implements Serializable {
        private static final long serialVersionUID = -4459795985457420810L;

        private long id;
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

        public long getId() {
            return id;
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

        public void setId(long id) {
            this.id = id;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, name, href, code, type, court);
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
                    Objects.equal(this.name, other.name) &&
                    Objects.equal(this.href, other.href) &&
                    Objects.equal(this.code, other.code) &&
                    Objects.equal(this.type, other.type) &&
                    Objects.equal(this.court, other.court);
        }


        //------------------------ toString --------------------------


        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
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
        private long id;
        private String name;
        private String code;
        private CommonCourtType type;

        //------------------------ GETTERS --------------------------

        public String getCode() {
            return code;
        }

        public CommonCourtType getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getHref() {
            return href;
        }

        public long getId() {
            return id;
        }

        //------------------------ SETTERS --------------------------

        public void setCode(String code) {
            this.code = code;
        }

        public void setType(CommonCourtType type) {
            this.type = type;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void setId(long id) {
            this.id = id;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, name, code, type, href);
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
                    Objects.equal(this.code, other.code) &&
                    Objects.equal(this.type, other.type) &&
                    Objects.equal(this.href, other.href) &&
                    Objects.equal(this.name, other.name)
                    ;
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("href", href)
                    .add("code", code)
                    .add("type", type)
                    .add("name", name)
                    .toString();
        }
    }

}
