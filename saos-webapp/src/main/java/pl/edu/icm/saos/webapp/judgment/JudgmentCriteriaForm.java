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
    
    private String courtId;
    private String courtName;
    
    private String judgeName;
    
    private String keyword;
    private String legalBase;
    private String referencedRegulation;
    
    
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
	
	public String getCourtId() {
		return courtId;
	}
	
	public String getCourtName() {
		return courtName;
	}
	
	public String getJudgeName() {
		return judgeName;
	}
	
	public String getKeyword() {
		return keyword;
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

	public void setCourtId(String courtId) {
		this.courtId = courtId;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	public void setJudgeName(String judgeName) {
		this.judgeName = judgeName;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setLegalBase(String legalBase) {
		this.legalBase = legalBase;
	}

	public void setReferencedRegulation(String referencedRegulation) {
		this.referencedRegulation = referencedRegulation;
	}

        
}

