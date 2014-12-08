package pl.edu.icm.saos.api.single.judgment.data.representation;

import java.io.Serializable;
import java.util.List;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SourceCode;

import com.google.common.base.Objects;

/**
 * Represent common {@link pl.edu.icm.saos.persistence.model.Judgment Judgmnet} fields.
 * @author pavtel
 */
public class JudgmentData implements Serializable{

    private static final long serialVersionUID = 6335902286597178584L;

    private int id;
    private CourtType courtType;
    private String href;
    private List<CourtCase> courtCases;
    private JudgmentType judgmentType;
    private String judgmentDate;
    private List<Judge> judges;
    private Source source;
    private List<String> courtReporters;
    private String decision;
    private String summary;
    private String textContent;
    private List<String> legalBases;
    private List<ReferencedRegulations> referencedRegulations;
    private List<String> keywords;


    //------------------------ GETTERS --------------------------

    public String getHref() {
        return href;
    }

    public List<CourtCase> getCourtCases() {
        return courtCases;
    }

    public JudgmentType getJudgmentType() {
        return judgmentType;
    }

    public String getJudgmentDate() {
        return judgmentDate;
    }

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

    public List<ReferencedRegulations> getReferencedRegulations() {
        return referencedRegulations;
    }

    public List<Judge> getJudges() {
        return judges;
    }

    public int getId() {
        return id;
    }

    public CourtType getCourtType() {
        return courtType;
    }
    
    public List<String> getKeywords() {
        return keywords;
    }

    //------------------------ SETTERS --------------------------

    public void setHref(String href) {
        this.href = href;
    }

    public void setCourtCases(List<CourtCase> courtCases) {
        this.courtCases = courtCases;
    }

    public void setJudgmentType(JudgmentType judgmentType) {
        this.judgmentType = judgmentType;
    }

    public void setJudgmentDate(String judgmentDate) {
        this.judgmentDate = judgmentDate;
    }

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

    public void setReferencedRegulations(List<ReferencedRegulations> ReferencedRegulations) {
        this.referencedRegulations = ReferencedRegulations;
    }

    public void setJudges(List<Judge> judges) {
        this.judges = judges;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }
    
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    



    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return Objects.hashCode(id, courtType,
                href, courtCases, judgmentType,
                judgmentDate, judges,
                source, courtReporters,
                decision, summary, textContent,
                legalBases, referencedRegulations, keywords);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final JudgmentData other = (JudgmentData) obj;
        return Objects.equal(this.id, other.id) &&
                Objects.equal(this.courtType, other.courtType) &&
                Objects.equal(this.href, other.href) &&
                Objects.equal(this.courtCases, other.courtCases)
                && Objects.equal(this.judgmentType, other.judgmentType)
                && Objects.equal(this.judgmentDate, other.judgmentDate)
                && Objects.equal(this.judges, other.judges)
                && Objects.equal(this.source, other.source)
                && Objects.equal(this.courtReporters, other.courtReporters)
                && Objects.equal(this.decision, other.decision)
                && Objects.equal(this.summary, other.summary)
                && Objects.equal(this.textContent, other.textContent)
                && Objects.equal(this.legalBases, other.legalBases)
                && Objects.equal(this.referencedRegulations, other.referencedRegulations)
                && Objects.equal(this.keywords, other.keywords);
    }


    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("courtType", courtType)
                .add("href", href)
                .add("courtCases", courtCases)
                .add("judgmentType", judgmentType)
                .add("judgmentDate", judgmentDate)
                .add("judges", judges)
                .add("source", source)
                .add("courtReporters", courtReporters)
                .add("decision", decision)
                .add("summary", summary)
                .add("textContent", textContent)
                .add("legalBases", legalBases)
                .add("referencedRegulation", referencedRegulations)
                .toString();
    }


    //------------------------ inner --------------------------

    public static class Judge implements Serializable {

        private static final long serialVersionUID = 1L;
        
        private String name;
        private List<String> specialRoles;

        //------------------------ GETTERS --------------------------

        public String getName() {
            return name;
        }

        public List<String> getSpecialRoles() {
            return specialRoles;
        }

        //------------------------ SETTERS --------------------------

        public void setName(String name) {
            this.name = name;
        }

        public void setSpecialRoles(List<String> specialRoles) {
            this.specialRoles = specialRoles;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(name, specialRoles);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Judge other = (Judge) obj;
            return Objects.equal(this.name, other.name) &&
                    Objects.equal(this.specialRoles, other.specialRoles);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("name", name)
                    .add("specialRoles", specialRoles)
                    .toString();
        }
    }

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

    public static class CourtCase implements Serializable {

        private static final long serialVersionUID = -7170635841783283728L;

        private String caseNumber;

        //------------------------ GETTERS --------------------------

        public String getCaseNumber() {
            return caseNumber;
        }

        //------------------------ SETTERS --------------------------

        public void setCaseNumber(String caseNumber) {
            this.caseNumber = caseNumber;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(caseNumber);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final CourtCase other = (CourtCase) obj;
            return Objects.equal(this.caseNumber, other.caseNumber);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("caseNumber", caseNumber)
                    .toString();
        }
    }

    public static class ReferencedRegulations implements Serializable{

        private static final long serialVersionUID = -6830086044877716672L;

        private String journalTitle;
        private int journalNo;
        private int journalYear;
        private int journalEntry;
        private String text;

        //------------------------ GETTERS --------------------------

        public String getJournalTitle() {
            return journalTitle;
        }

        public int getJournalNo() {
            return journalNo;
        }

        public int getJournalYear() {
            return journalYear;
        }

        public int getJournalEntry() {
            return journalEntry;
        }

        public String getText() {
            return text;
        }

        //------------------------ SETTERS --------------------------

        public void setJournalTitle(String journalTitle) {
            this.journalTitle = journalTitle;
        }

        public void setJournalNo(int journalNo) {
            this.journalNo = journalNo;
        }

        public void setJournalYear(int journalYear) {
            this.journalYear = journalYear;
        }

        public void setJournalEntry(int journalEntry) {
            this.journalEntry = journalEntry;
        }

        public void setText(String text) {
            this.text = text;
        }

        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(journalTitle, journalNo, journalYear, journalEntry, text);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final ReferencedRegulations other = (ReferencedRegulations) obj;
            return Objects.equal(this.journalTitle, other.journalTitle) &&
                    Objects.equal(this.journalNo, other.journalNo) &&
                    Objects.equal(this.journalYear, other.journalYear) &&
                    Objects.equal(this.journalEntry, other.journalEntry) &&
                    Objects.equal(this.text, other.text);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("journalTitle", journalTitle)
                    .add("journalNo", journalNo)
                    .add("journalYear", journalYear)
                    .add("journalEntry", journalEntry)
                    .add("text", text)
                    .toString();
        }
    }

   

}
