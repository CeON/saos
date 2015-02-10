package pl.edu.icm.saos.search.search.service;

import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

public class JudgmentCriteriaBuilder {

    private JudgmentCriteria criteria;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JudgmentCriteriaBuilder() {
        criteria = new JudgmentCriteria();
    }
    
    public JudgmentCriteriaBuilder(String all) {
        criteria = new JudgmentCriteria(all);
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public JudgmentCriteria build() {
        return criteria;
    }
    
    public JudgmentCriteriaBuilder withAll(String all) {
        criteria.setAll(all);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCaseNumber(String caseNumber) {
        criteria.setCaseNumber(caseNumber);
        return this;
    }
    
    public JudgmentCriteriaBuilder withDateFrom(LocalDate dateFrom) {
        criteria.setDateFrom(dateFrom);
        return this;
    }
    
    public JudgmentCriteriaBuilder withDateTo(LocalDate dateTo) {
        criteria.setDateTo(dateTo);
        return this;
    }
    
    public JudgmentCriteriaBuilder withDateRange(LocalDate dateFrom, LocalDate dateTo) {
        criteria.setDateFrom(dateFrom);
        criteria.setDateTo(dateTo);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCourtType(CourtType courtType) {
        criteria.setCourtType(courtType);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCcCourtType(CommonCourtType commonCourtType) {
        criteria.setCcCourtType(commonCourtType);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCcCourtId(long courtId) {
        criteria.setCcCourtId(courtId);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCcCourtCode(String courtCode) {
        criteria.setCcCourtCode(courtCode);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCcCourtName(String courtName) {
        criteria.setCcCourtName(courtName);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCcDivisionId(long divisionId) {
        criteria.setCcCourtDivisionId(divisionId);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCcDivisionCode(String divisionCode) {
        criteria.setCcCourtDivisionCode(divisionCode);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCcDivisionName(String divisionName) {
        criteria.setCcCourtDivisionName(divisionName);
        return this;
    }
    
    public JudgmentCriteriaBuilder withScJudgmentForm(String judgmentForm) {
        criteria.setScJudgmentForm(judgmentForm);
        return this;
    }
    
    public JudgmentCriteriaBuilder withScPersonnelType(PersonnelType personnelType) {
        criteria.setScPersonnelType(personnelType);
        return this;
    }
    
    public JudgmentCriteriaBuilder withScChamberId(long chamberId) {
        criteria.setScCourtChamberId(chamberId);
        return this;
    }
    
    public JudgmentCriteriaBuilder withScChamberName(String chamberName) {
        criteria.setScCourtChamberName(chamberName);
        return this;
    }
    
    public JudgmentCriteriaBuilder withScChamberDivisionId(long chamberDivisionId) {
        criteria.setScCourtChamberDivisionId(chamberDivisionId);
        return this;
    }
    
    public JudgmentCriteriaBuilder withScChamberDivisionName(String chamberDivisionName) {
        criteria.setScCourtChamberDivisionName(chamberDivisionName);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCtDissentingOpinion(String dissentingOpinion) {
        criteria.setCtDissentingOpinion(dissentingOpinion);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCtDissentingOpinionAuthor(String dissentingOpinionAuthor) {
        criteria.setCtDissentingOpinionAuthor(dissentingOpinionAuthor);
        return this;
    }
    
    public JudgmentCriteriaBuilder withJudgmentType(JudgmentType type) {
        criteria.addJudgmentType(type);
        return this;
    }
    
    public JudgmentCriteriaBuilder withJudgeName(String judgeName) {
        criteria.setJudgeName(judgeName);
        return this;
    }
    
    public JudgmentCriteriaBuilder withKeyword(String keyword) {
        criteria.addKeyword(keyword);
        return this;
    }
    
    public JudgmentCriteriaBuilder withLegalBase(String legalBase) {
        criteria.setLegalBase(legalBase);
        return this;
    }
    
    public JudgmentCriteriaBuilder withReferencedRegulation(String referencedRegulation) {
        criteria.setReferencedRegulation(referencedRegulation);
        return this;
    }
    
    public JudgmentCriteriaBuilder withLawJournalEntryId(long lawJournalEntryId) {
        criteria.setLawJournalEntryId(lawJournalEntryId);
        return this;
    }
    
}
