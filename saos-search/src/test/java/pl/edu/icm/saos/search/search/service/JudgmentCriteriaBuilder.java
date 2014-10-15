package pl.edu.icm.saos.search.search.service;

import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.search.search.model.CourtType;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

public class JudgmentCriteriaBuilder {

    private JudgmentCriteria criteria;
    
    public JudgmentCriteriaBuilder() {
        criteria = new JudgmentCriteria();
    }
    
    public JudgmentCriteriaBuilder(String all) {
        criteria = new JudgmentCriteria(all);
    }
    
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
    
    public JudgmentCriteriaBuilder withCourtId(int courtId) {
        criteria.setCourtId(courtId);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCourtCode(String courtCode) {
        criteria.setCourtCode(courtCode);
        return this;
    }
    
    public JudgmentCriteriaBuilder withCourtName(String courtName) {
        criteria.setCourtName(courtName);
        return this;
    }
    
    public JudgmentCriteriaBuilder withDivisionId(int divisionId) {
        criteria.setCourtDivisionId(divisionId);
        return this;
    }
    
    public JudgmentCriteriaBuilder withDivisionCode(String divisionCode) {
        criteria.setCourtDivisionCode(divisionCode);
        return this;
    }
    
    public JudgmentCriteriaBuilder withDivisionName(String divisionName) {
        criteria.setCourtDivisionName(divisionName);
        return this;
    }
    
    public JudgmentCriteriaBuilder withPersonnelType(PersonnelType personnelType) {
        criteria.setPersonnelType(personnelType);
        return this;
    }
    
    public JudgmentCriteriaBuilder withChamberId(int chamberId) {
        criteria.setCourtChamberId(chamberId);
        return this;
    }
    
    public JudgmentCriteriaBuilder withChamberName(String chamberName) {
        criteria.setCourtChamberName(chamberName);
        return this;
    }
    
    public JudgmentCriteriaBuilder withChamberDivisionId(int chamberDivisionId) {
        criteria.setCourtChamberDivisionId(chamberDivisionId);
        return this;
    }
    
    public JudgmentCriteriaBuilder withChamberDivisionName(String chamberDivisionName) {
        criteria.setCourtChamberDivisionName(chamberDivisionName);
        return this;
    }
    
    public JudgmentCriteriaBuilder withJudgmentType(JudgmentType type) {
        criteria.setJudgmentType(type);
        return this;
    }
    
    public JudgmentCriteriaBuilder withJudgeName(String judgeName) {
        criteria.setJudgeName(judgeName);
        return this;
    }
    
    public JudgmentCriteriaBuilder withKeyword(String keyword) {
        criteria.setKeyword(keyword);
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
    
}
