package pl.edu.icm.saos.api.search.judgments.item.representation;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import static pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData.CourtCase;
import static pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData.Judge;

/**
 * R Represents single item in items field in the
 * {@link pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView SearchJudgmentsView}.
 * @author pavtel
 */
public class SearchJudgmentItem implements Serializable{

    private static final long serialVersionUID = -4979928756161430538L;

    private Long id;
    private String href;
    private CourtType courtType;
    private List<CourtCase> courtCases;
    private JudgmentType judgmentType;
    private String JudgmentDate;
    private List<Judge> judges;
    private String textContent;
    private List<String> keywords;


    //------------------------ GETTERS --------------------------

    public Long getId() {
        return id;
    }

    public String getHref() {
        return href;
    }

    public CourtType getCourtType() {
        return courtType;
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

    public String getTextContent() {
        return textContent;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    //------------------------ SETTERS --------------------------

    public void setId(Long id) {
        this.id = id;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
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

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return Objects.hashCode(id, href, courtType, courtCases, judgmentType, JudgmentDate, judges, textContent, keywords);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SearchJudgmentItem other = (SearchJudgmentItem) obj;
        return Objects.equal(this.id, other.id) &&
                Objects.equal(this.href, other.href) &&
                Objects.equal(this.courtType, other.courtType) &&
                Objects.equal(this.courtCases, other.courtCases) &&
                Objects.equal(this.judgmentType, other.judgmentType) &&
                Objects.equal(this.JudgmentDate, other.JudgmentDate) &&
                Objects.equal(this.judges, other.judges) &&
                Objects.equal(this.textContent, other.textContent) &&
                Objects.equal(this.keywords, other.keywords);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("href", href)
                .add("courtType", courtType)
                .add("courtCases", courtCases)
                .add("judgmentType", judgmentType)
                .add("JudgmentDate", JudgmentDate)
                .add("judges", judges)
                .add("textContent", textContent)
                .add("keywords", keywords)
                .toString();
    }
}
