package pl.edu.icm.saos.api.search.judgments.item.representation;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;

import static pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData.CourtCase;
import static pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData.Judge;

/**
 * R Represents single item in items field in the
 * {@link pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView SearchJudgmentsView}.
 * @author pavtel
 */
public class SearchJudgmentItem implements Serializable{

    private static final long serialVersionUID = -4979928756161430538L;

    protected String href;
    protected List<CourtCase> courtCases;
    protected String judgmentType;
    protected String JudgmentDate;
    protected List<Judge> judges;
    protected String textContent;
    protected List<String> keywords;

    //------------------------ GETTERS --------------------------

    public String getHref() {
        return href;
    }

    public List<CourtCase> getCourtCases() {
        return courtCases;
    }

    public String getJudgmentType() {
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

    public void setHref(String href) {
        this.href = href;
    }

    public void setCourtCases(List<CourtCase> courtCases) {
        this.courtCases = courtCases;
    }

    public void setJudgmentType(String judgmentType) {
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
        return Objects.hashCode(href, courtCases, judgmentType, JudgmentDate, judges, textContent, keywords);
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
        return Objects.equal(this.href, other.href) &&
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
                .add("href", href)
                .add("courtCases", courtCases)
                .add("judgmentType", judgmentType)
                .add("JudgmentDate", JudgmentDate)
                .add("judges", judges)
                .add("textContent", textContent)
                .add("keywords", keywords)
                .toString();
    }
}
