package pl.edu.icm.saos.api.search.judgments.parameters;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author pavtel
 */
public class RequestJudgmentsParameters {

    private int pageSize;
    private int pageNumber;
    private String all;
    private String courtName;
    private String legalBase;
    private String referencedRegulation;
    private String judgeName;
    private String keyword;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate judgmentDateFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate judgmentDateTo;

    //------------------------ GETTERS --------------------------
    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getAll() {
        return all;
    }

    public String getCourtName() {
        return courtName;
    }

    public String getLegalBase() {
        return legalBase;
    }

    public String getReferencedRegulation() {
        return referencedRegulation;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public String getKeyword() {
        return keyword;
    }

    public LocalDate getJudgmentDateFrom() {
        return judgmentDateFrom;
    }

    public LocalDate getJudgmentDateTo() {
        return judgmentDateTo;
    }

    //------------------------ SETTERS --------------------------

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public void setLegalBase(String legalBase) {
        this.legalBase = legalBase;
    }

    public void setReferencedRegulation(String referencedRegulation) {
        this.referencedRegulation = referencedRegulation;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setJudgmentDateFrom(LocalDate judgmentDateFrom) {
        this.judgmentDateFrom = judgmentDateFrom;
    }

    public void setJudgmentDateTo(LocalDate judgmentDateTo) {
        this.judgmentDateTo = judgmentDateTo;
    }
}
