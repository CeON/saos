package pl.edu.icm.saos.api.single.judgment.representation;

import com.google.common.base.Objects;
import pl.edu.icm.saos.persistence.model.SourceCode;

import java.io.Serializable;
import java.util.List;

/**
 * Represent common {@link pl.edu.icm.saos.persistence.model.Judgment Judgmnet} fields.
 * @author pavtel
 */
public class CommonFieldsData  implements Serializable{

    protected Source source;
    protected List<String> courtReporters;
    protected String decision;
    protected String summary;
    protected String textContent;
    protected List<String> legalBases;
    protected List<ReferencedRegulation> referencedRegulation;
    protected List<String> keywords;

    //------------------------ GETTERS --------------------------

    public Source getSource() {
        return source;
    }

    public List<String> getCourtReporters() {
        return courtReporters;
    }

    public String getDecision() {
        return decision;
    }

    public String getSummary() {
        return summary;
    }

    public String getTextContent() {
        return textContent;
    }

    public List<String> getLegalBases() {
        return legalBases;
    }

    public List<ReferencedRegulation> getReferencedRegulation() {
        return referencedRegulation;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    //------------------------ SETTERS --------------------------

    public void setSource(Source source) {
        this.source = source;
    }

    public void setCourtReporters(List<String> courtReporters) {
        this.courtReporters = courtReporters;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setLegalBases(List<String> legalBases) {
        this.legalBases = legalBases;
    }

    public void setReferencedRegulation(List<ReferencedRegulation> referencedRegulation) {
        this.referencedRegulation = referencedRegulation;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    //------------------------ HashCode & Equals --------------------------


    @Override
    public int hashCode() {
        return Objects.hashCode(source, courtReporters, decision, summary, textContent, legalBases, referencedRegulation, keywords);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CommonFieldsData other = (CommonFieldsData) obj;
        return Objects.equal(this.source, other.source) &&
                Objects.equal(this.courtReporters, other.courtReporters) &&
                Objects.equal(this.decision, other.decision) &&
                Objects.equal(this.summary, other.summary) &&
                Objects.equal(this.textContent, other.textContent) &&
                Objects.equal(this.legalBases, other.legalBases) &&
                Objects.equal(this.referencedRegulation, other.referencedRegulation) &&
                Objects.equal(this.keywords, other.keywords);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("source", source)
                .add("courtReporters", courtReporters)
                .add("decision", decision)
                .add("summary", summary)
                .add("textContent", textContent)
                .add("legalBases", legalBases)
                .add("referencedRegulations", referencedRegulation)
                .add("keywords", keywords)
                .toString();
    }


    //------------------------ inner --------------------------

    public static class Source implements Serializable {

        private static final long serialVersionUID = 6690231920405411761L;

        private SourceCode code;
        private String judgmentUrl;
        private String judgmentId;
        private String publisher;
        private String reviser;
        private String publicationDate;

        //------------------------ GETTERS --------------------------

        public SourceCode getCode() {
            return code;
        }

        public String getJudgmentUrl() {
            return judgmentUrl;
        }

        public String getJudgmentId() {
            return judgmentId;
        }

        public String getPublisher() {
            return publisher;
        }

        public String getReviser() {
            return reviser;
        }

        public String getPublicationDate() {
            return publicationDate;
        }

        //------------------------ SETTERS --------------------------

        public void setCode(SourceCode code) {
            this.code = code;
        }

        public void setJudgmentUrl(String judgmentUrl) {
            this.judgmentUrl = judgmentUrl;
        }

        public void setJudgmentId(String judgmentId) {
            this.judgmentId = judgmentId;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public void setReviser(String reviser) {
            this.reviser = reviser;
        }

        public void setPublicationDate(String publicationDate) {
            this.publicationDate = publicationDate;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(code, judgmentUrl, judgmentId, publisher, reviser, publicationDate);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Source other = (Source) obj;
            return Objects.equal(this.code, other.code) &&
                    Objects.equal(this.judgmentUrl, other.judgmentUrl) &&
                    Objects.equal(this.judgmentId, other.judgmentId) &&
                    Objects.equal(this.publisher, other.publisher) &&
                    Objects.equal(this.reviser, other.reviser) &&
                    Objects.equal(this.publicationDate, other.publicationDate);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("code", code)
                    .add("judgmentUrl", judgmentUrl)
                    .add("judgmentId", judgmentId)
                    .add("publisher", publisher)
                    .add("reviser", reviser)
                    .add("publicationDate", publicationDate)
                    .toString();
        }
    }

    public static class ReferencedRegulation implements Serializable{

        private String text;

    }

    public static class LawJournalEntry
}
