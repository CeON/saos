package pl.edu.icm.saos.api.dump.judgment.item.representation;

import java.io.Serializable;
import java.util.List;

import pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData;
import pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData.CourtCase;
import pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData.Judge;
import pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData.ReferencedRegulation;
import pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData.Source;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;

import com.google.common.base.Objects;

/**
 * Represents item in items field in the {@link pl.edu.icm.saos.api.dump.judgment.views.DumpJudgmentsView DumpJudgmentsView}.
 * @author pavtel
 */
public class JudgmentItem implements Serializable {
    private static final long serialVersionUID = -7120554748494835309L;

    private long id;
    private CourtType courtType;
    private List<CourtCase> courtCases;
    private JudgmentType judgmentType;
    private String JudgmentDate;
    private List<Judge> judges;
    private Source source;
    private List<String> courtReporters;
    private String decision;
    private String summary;
    private String textContent;
    private List<String> legalBases;
    private List<ReferencedRegulation> referencedRegulations;
    private List<String> keywords;
    private List<JudgmentData.ReferencedCourtCase> referencedCourtCases;
    private String receiptDate;
    private String meansOfAppeal;
    private String judgmentResult;
    private List<String> lowerCourtJudgments;


    //------------------------ GETTERS --------------------------
    
    public long getId() {
        return id;
    }

    public List<CourtCase> getCourtCases() {
        return courtCases;
    }

    public JudgmentType getJudgmentType() {
        return judgmentType;
    }

    public String getJudgmentDate() {
        return JudgmentDate;
    }

    public List<Judge> getJudges() {
        return judges;
    }

    public Source getSource() {
        return source;
    }

    public List<String> getCourtReporters() {
        return courtReporters;
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

    public List<ReferencedRegulation> getReferencedRegulations() {
        return referencedRegulations;
    }

    public String getDecision() {
        return decision;
    }

    public CourtType getCourtType() {
        return courtType;
    }
    
    public List<String> getKeywords() {
        return keywords;
    }
    
    public List<JudgmentData.ReferencedCourtCase> getReferencedCourtCases() {
        return referencedCourtCases;
    }
    
    public String getReceiptDate() {
        return receiptDate;
    }

    public String getMeansOfAppeal() {
        return meansOfAppeal;
    }

    public String getJudgmentResult() {
        return judgmentResult;
    }

    public List<String> getLowerCourtJudgments() {
        return lowerCourtJudgments;
    }

    

    //------------------------ SETTERS --------------------------

    public void setId(long id) {
        this.id = id;
    }

    public void setCourtCases(List<CourtCase> courtCases) {
        this.courtCases = courtCases;
    }

    public void setJudgmentType(JudgmentType judgmentType) {
        this.judgmentType = judgmentType;
    }

    public void setJudgmentDate(String judgmentDate) {
        JudgmentDate = judgmentDate;
    }

    public void setJudges(List<Judge> judges) {
        this.judges = judges;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public void setCourtReporters(List<String> courtReporters) {
        this.courtReporters = courtReporters;
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

    public void setReferencedRegulations(List<ReferencedRegulation> referencedRegulations) {
        this.referencedRegulations = referencedRegulations;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }
    
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    
    public void setReferencedCourtCases(List<JudgmentData.ReferencedCourtCase> referencedCourtCases) {
        this.referencedCourtCases = referencedCourtCases;
    }
    
    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public void setMeansOfAppeal(String meansOfAppeal) {
        this.meansOfAppeal = meansOfAppeal;
    }

    public void setJudgmentResult(String judgmentResult) {
        this.judgmentResult = judgmentResult;
    }

    public void setLowerCourtJudgments(List<String> lowerCourtJudgments) {
        this.lowerCourtJudgments = lowerCourtJudgments;
    }


    //------------------------ HashCode & Equals --------------------------


    @Override
    public int hashCode() {
        return Objects.hashCode(id, courtCases, judgmentType, JudgmentDate,
                judges, source, courtReporters, summary, decision,
                textContent, legalBases, referencedRegulations,
                courtType, keywords, referencedCourtCases,
                receiptDate, meansOfAppeal, judgmentResult, lowerCourtJudgments
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final JudgmentItem other = (JudgmentItem) obj;
        return Objects.equal(this.id, other.id) &&
                Objects.equal(this.courtCases, other.courtCases) &&
                Objects.equal(this.judgmentType, other.judgmentType) &&
                Objects.equal(this.JudgmentDate, other.JudgmentDate) &&
                Objects.equal(this.judges, other.judges) &&
                Objects.equal(this.source, other.source) &&
                Objects.equal(this.decision, other.decision) &&
                Objects.equal(this.courtReporters, other.courtReporters) &&
                Objects.equal(this.summary, other.summary) &&
                Objects.equal(this.textContent, other.textContent) &&
                Objects.equal(this.legalBases, other.legalBases) &&
                Objects.equal(this.referencedRegulations, other.referencedRegulations) &&
                Objects.equal(this.courtType, other.courtType) &&
                Objects.equal(this.keywords, other.keywords) &&
                Objects.equal(this.referencedCourtCases, other.referencedCourtCases) &&
                Objects.equal(this.receiptDate, other.receiptDate) &&
                Objects.equal(this.meansOfAppeal, other.meansOfAppeal) &&
                Objects.equal(this.judgmentResult, other.judgmentResult) &&
                Objects.equal(this.lowerCourtJudgments, other.lowerCourtJudgments)
                ;
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
                .add("courtType", courtType)
                .add("referencedCourtCases", referencedCourtCases)
                .add("receiptDate", receiptDate)
                .add("meansOfAppeal", meansOfAppeal)
                .add("judgmentResult", judgmentResult)
                .add("lowerCourtJudgments", lowerCourtJudgments)
                .toString();
    }

   

}
