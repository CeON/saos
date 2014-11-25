package pl.edu.icm.saos.webapp.judgment;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pl.edu.icm.saos.persistence.common.FormatConst;
import pl.edu.icm.saos.persistence.model.CourtType;


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
    private String keyword;
    private CourtType courtType;
    
    private Integer commonCourtId;
    private Integer commonCourtDivisionId;
    
    private String scJudgmentForm;
    private Integer supremeChamberId;
    private Integer supremeChamberDivisionId;
    
    private String[] judgmentType;
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
	
	public String getKeyword() {
		return keyword;
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
	
	public String getScJudgmentForm() {
		return scJudgmentForm;
	}
	
	public Integer getSupremeChamberId() {
		return supremeChamberId;
	}
	
	public Integer getSupremeChamberDivisionId() {
		return supremeChamberDivisionId;
	}
	
	public String[] getJudgmentType() {
		return judgmentType;
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

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public void setScJudgmentForm(String scJudgmentForm) {
		this.scJudgmentForm = scJudgmentForm;
	}
	
	public void setSupremeChamberId(Integer supremeChamberId) {
		this.supremeChamberId = supremeChamberId;
	}

	public void setSupremeChamberDivisionId(Integer supremeChamberDivisionId) {
		this.supremeChamberDivisionId = supremeChamberDivisionId;
	}
	
	public void setJudgmentType(String[] judgmentType) {
		this.judgmentType = judgmentType;
	}

	public void setLegalBase(String legalBase) {
		this.legalBase = legalBase;
	}

	public void setReferencedRegulation(String referencedRegulation) {
		this.referencedRegulation = referencedRegulation;
	}

        
}

