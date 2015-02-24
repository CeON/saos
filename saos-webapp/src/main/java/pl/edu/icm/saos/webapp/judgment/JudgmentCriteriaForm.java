package pl.edu.icm.saos.webapp.judgment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pl.edu.icm.saos.persistence.common.FormatConst;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.webapp.format.MultiWordFormat;

import com.google.common.collect.Lists;



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
    private List<String> keywords = Lists.newArrayList();
    
    private CourtType courtType;
    
    private Long commonCourtId;
    private Long commonCourtDivisionId;
    
    private PersonnelType scPersonnelType;
    private String scJudgmentForm;
    private Long supremeChamberId;
    private Long supremeChamberDivisionId;
    
    private String ctDissentingOpinion;
    
    private Set<JudgmentType> judgmentTypes = new HashSet<JudgmentType>();
    
    private String legalBase;
    private String referencedRegulation;
    private Long lawJournalEntryId;
    
    
    
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
		if (keywords == null) {
			return Lists.newArrayList();
		} else {
			return keywords;
		}
	}
	
	public CourtType getCourtType() {
		return courtType;
	}
	
	public Long getCommonCourtId() {
		return commonCourtId;
	}
	
	public Long getCommonCourtDivisionId() {
		return commonCourtDivisionId;
	}
	
	public PersonnelType getScPersonnelType() {
		return scPersonnelType;
	}
	
	public String getScJudgmentForm() {
		return scJudgmentForm;
	}
	
	public Long getSupremeChamberId() {
		return supremeChamberId;
	}
	
	public Long getSupremeChamberDivisionId() {
		return supremeChamberDivisionId;
	}
	
    public String getCtDissentingOpinion() {
        return ctDissentingOpinion;
    }
	
	public Set<JudgmentType> getJudgmentTypes() {
		if (judgmentTypes == null) {
			return new HashSet<JudgmentType>();
		} else {
			return judgmentTypes;	
		}
	}
	
	public String getLegalBase() {
		return legalBase;
	}
	
	public String getReferencedRegulation() {
		return referencedRegulation;
	}
	
	public Long getLawJournalEntryId() {
		return lawJournalEntryId;
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
	
	public void setCommonCourtId(Long commonCourtId) {
		this.commonCourtId = commonCourtId;
	}
	
	public void setCommonCourtDivisionId(Long commonCourtDivisionId) {
		this.commonCourtDivisionId = commonCourtDivisionId;
	}
	
	public void setScPersonnelType(PersonnelType scPersonnelType) {
		this.scPersonnelType = scPersonnelType;
	}

	public void setScJudgmentForm(String scJudgmentForm) {
		this.scJudgmentForm = scJudgmentForm;
	}
	
	public void setSupremeChamberId(Long supremeChamberId) {
		this.supremeChamberId = supremeChamberId;
	}

	public void setSupremeChamberDivisionId(Long supremeChamberDivisionId) {
		this.supremeChamberDivisionId = supremeChamberDivisionId;
	}
	
    public void setCtDissentingOpinion(String ctDissentingOpinion) {
        this.ctDissentingOpinion = ctDissentingOpinion;
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
	
	public void setLawJournalEntryId(Long lawJournalEntryId) {
		this.lawJournalEntryId = lawJournalEntryId;
	}

}

