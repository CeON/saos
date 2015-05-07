package pl.edu.icm.saos.webapp.judgment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pl.edu.icm.saos.persistence.common.FormatConst;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.webapp.common.search.CourtCriteria;
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
    
    
    private CourtCriteria courtCriteria = new CourtCriteria();
    
    
    private PersonnelType scPersonnelType;
    private String scJudgmentForm;
    
    private String ctDissentingOpinion;
    
    private Set<JudgmentType> judgmentTypes = new HashSet<JudgmentType>();
    
    private String legalBase;
    private String referencedRegulation;
    private Long lawJournalEntryId;
    private Long referencedCourtCaseId;
    
    
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
	
	public PersonnelType getScPersonnelType() {
		return scPersonnelType;
	}
	
	public String getScJudgmentForm() {
		return scJudgmentForm;
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
	
    public Long getReferencedCourtCaseId() {
        return referencedCourtCaseId;
    }
	
    public CourtCriteria getCourtCriteria() {
        return courtCriteria;
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

	public void setScPersonnelType(PersonnelType scPersonnelType) {
		this.scPersonnelType = scPersonnelType;
	}

	public void setScJudgmentForm(String scJudgmentForm) {
		this.scJudgmentForm = scJudgmentForm;
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

    public void setReferencedCourtCaseId(Long referencedCourtCaseId) {
        this.referencedCourtCaseId = referencedCourtCaseId;
    }

    public void setCourtCriteria(CourtCriteria courtCriteria) {
        this.courtCriteria = courtCriteria;
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return "JudgmentCriteriaForm [all=" + all + ", signature=" + signature + ", dateFrom="
                + dateFrom + ", dateTo=" + dateTo + ", judgeName=" + judgeName + ", keywords="
                + keywords + ", courtCriteria=" + courtCriteria + ", scPersonnelType="
                + scPersonnelType + ", scJudgmentForm=" + scJudgmentForm + ", ctDissentingOpinion="
                + ctDissentingOpinion + ", judgmentTypes=" + judgmentTypes + ", legalBase="
                + legalBase + ", referencedRegulation=" + referencedRegulation
                + ", lawJournalEntryId=" + lawJournalEntryId + ", referencedCourtCaseId="
                + referencedCourtCaseId + "]";
    }

    
}

