package pl.edu.icm.saos.api.single.judgment.data.representation;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;

/**
 * Represents {@link pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment ConstitutionalTribunalJudgment} fields.
 * @author pavtel
 */
public class ConstitutionalTribunalJudgmentData extends JudgmentData{
    private static final long serialVersionUID = 3202843536990337571L;

    private List<DissentingOpinion> dissentingOpinions;

    //------------------------ GETTERS --------------------------


    public List<DissentingOpinion> getDissentingOpinions() {
        return dissentingOpinions;
    }
    //------------------------ SETTERS --------------------------


    public void setDissentingOpinions(List<DissentingOpinion> dissentingOpinions) {
        this.dissentingOpinions = dissentingOpinions;
    }

    //------------------------ HashCode & Equals --------------------------
    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(dissentingOpinions);
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
        final ConstitutionalTribunalJudgmentData other = (ConstitutionalTribunalJudgmentData) obj;
        return Objects.equal(this.dissentingOpinions, other.dissentingOpinions);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(super.toString())
                .add("dissentingOpinions", dissentingOpinions)
                .toString();
    }

    public static class DissentingOpinion implements Serializable {
        private static final long serialVersionUID = -7658444133092444276L;

        private String textContent;
        private List<String> authors;

        //------------------------ GETTERS --------------------------

        public String getTextContent() {
            return textContent;
        }

        public List<String> getAuthors() {
            return authors;
        }

        //------------------------ SETTERS --------------------------

        public void setTextContent(String textContent) {
            this.textContent = textContent;
        }

        public void setAuthors(List<String> authors) {
            this.authors = authors;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(textContent, authors);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final DissentingOpinion other = (DissentingOpinion) obj;
            return Objects.equal(this.textContent, other.textContent) &&
                    Objects.equal(this.authors, other.authors);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("textContent", textContent)
                    .add("authors", authors)
                    .toString();
        }
    }


}
