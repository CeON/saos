package pl.edu.icm.saos.webapp.judgment;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pl.edu.icm.saos.persistence.common.FormatConst;


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
    private String courtType;
    
    private String commonCourtId;
    private String commonCourtName;
    private String commonCourtDivisionId;
    
    private String supremeChamberId;
    private String supremeChamberDivisionId;
    
    private String[] judgmentType;
    private String legalBase;
    private String referencedRegulation;
    
    /*** Getters ***/
    
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
	
	public String getCourtType() {
		return courtType;
	}
	
	public String getCommonCourtId() {
		return commonCourtId;
	}
	
	public String getCommonCourtName() {
		return commonCourtName;
	}
	
	public String getCommonCourtDivisionId() {
		return commonCourtDivisionId;
	}
	
	public String getSupremeChamberId() {
		return supremeChamberId;
	}
	
	public String getSupremeChamberDivisionId() {
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
	
	/* Setters */
	
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

	public void setCourtType(String courtType) {
		this.courtType = courtType;
	}
	
	public void setCommonCourtId(String commonCourtId) {
		this.commonCourtId = commonCourtId;
	}

	public void setCommonCourtName(String commonCourtName) {
		this.commonCourtName = commonCourtName;
	}
	
	public void setCommonCourtDivisionId(String commonCourtDivisionId) {
		this.commonCourtDivisionId = commonCourtDivisionId;
	}

	public void setSupremeChamberId(String supremeChamberId) {
		this.supremeChamberId = supremeChamberId;
	}

	public void setSupremeChamberDivisionId(String supremeChamberDivisionId) {
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

