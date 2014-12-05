package pl.edu.icm.saos.webapp.judgment;

import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.common.FormatConst;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.webapp.format.MultiWordFormat;



/**
 * @author Łukasz Pawełczak
 *
 */
public class JudgmentCriteriaForm {
    
    private String all;
    
    private String signature;
    
    @DateTimeFormat(pattern=FormatConst.DATE_FORMAT_PATTERN)
    private LocalDate dateFrom;
    
    @DateTimeFormat(pattern=FormatConst.DATE_FORMAT_PATTERN)
    private LocalDate dateTo;
    
    private String judgeName;
    
    @MultiWordFormat
    private List<String> keywords;
    
    private CourtType courtType;
    
    private Integer commonCourtId;
    private Integer commonCourtDivisionId;
    
    private PersonnelType scPersonnelType;
    private String scJudgmentForm;
    private Integer supremeChamberId;
    private Integer supremeChamberDivisionId;
    
    
    private Set<JudgmentType> judgmentTypes;
    
    private String legalBase;
    private String referencedRegulation;
    
    
    
    
    //------------------------ GETTERS --------------------------
    
    public String getAll() {
		return all;
	}
    
	public String getSignature() {
		return signature;
	}
	
	public LocalDate getDateFrom() {
		return dateFrom;
	}
	
	public LocalDate getDateTo() {
		return dateTo;
	}
	
	public String getJudgeName() {
		return judgeName;
	}
	
	public List<String> getKeywords() {
		return keywords;
	}
	
	public CourtType getCourtType() {
		return courtType;
	}
	
	public Integer getCommonCourtId() {
		return commonCourtId;
	}
	
	public Integer getCommonCourtDivisionId() {
		return commonCourtDivisionId;
	}
	
	public PersonnelType getScPersonnelType() {
		return scPersonnelType;
	}
	
	public String getScJudgmentForm() {
		return scJudgmentForm;
	}
	
	public Integer getSupremeChamberId() {
		return supremeChamberId;
	}
	
	public Integer getSupremeChamberDivisionId() {
		return supremeChamberDivisionId;
	}
	
	public Set<JudgmentType> getJudgmentTypes() {
		return judgmentTypes;
	}
	
	public String getLegalBase() {
		return legalBase;
	}
	
	public String getReferencedRegulation() {
		return referencedRegulation;
	}
	
	//------------------------ SETTERS --------------------------
	
	public void setAll(String all) {
		this.all = all;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public void setDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}
	
	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
	}
	
	public void setJudgeName(String judgeName) {
		this.judgeName = judgeName;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public void setCourtType(CourtType courtType) {
		this.courtType = courtType;
	}
	
	public void setCommonCourtId(Integer commonCourtId) {
		this.commonCourtId = commonCourtId;
	}
	
	public void setCommonCourtDivisionId(Integer commonCourtDivisionId) {
		this.commonCourtDivisionId = commonCourtDivisionId;
	}
	
	public void setScPersonnelType(PersonnelType scPersonnelType) {
		this.scPersonnelType = scPersonnelType;
	}

	public void setScJudgmentForm(String scJudgmentForm) {
		this.scJudgmentForm = scJudgmentForm;
	}
	
	public void setSupremeChamberId(Integer supremeChamberId) {
		this.supremeChamberId = supremeChamberId;
	}

	public void setSupremeChamberDivisionId(Integer supremeChamberDivisionId) {
		this.supremeChamberDivisionId = supremeChamberDivisionId;
	}
	
	public void setJudgmentTypes(Set<JudgmentType> judgmentTypes) {
		this.judgmentTypes = judgmentTypes;
	}

	public void setLegalBase(String legalBase) {
		this.legalBase = legalBase;
	}

	public void setReferencedRegulation(String referencedRegulation) {
		this.referencedRegulation = referencedRegulation;
	}

        
}

