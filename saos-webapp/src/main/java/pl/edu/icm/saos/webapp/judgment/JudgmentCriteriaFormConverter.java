package pl.edu.icm.saos.webapp.judgment;


import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.webapp.common.search.CourtCriteria;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Pawełczak
 *
 */
@Service
public class JudgmentCriteriaFormConverter {

	
	//------------------------ LOGIC --------------------------
	
	public JudgmentCriteria convert(JudgmentCriteriaForm judgmentCriteriaForm) {
		JudgmentCriteria judgmentCriteria = new JudgmentCriteria();
		
		judgmentCriteria.setAll(judgmentCriteriaForm.getAll());
		judgmentCriteria.setCaseNumber(judgmentCriteriaForm.getSignature());
		judgmentCriteria.setJudgmentDateFrom(judgmentCriteriaForm.getDateFrom());
		judgmentCriteria.setJudgmentDateTo(judgmentCriteriaForm.getDateTo());
				
		judgmentCriteria.setJudgeName(judgmentCriteriaForm.getJudgeName());
		
		CourtCriteria courtCriteria = judgmentCriteriaForm.getCourtCriteria();
		
		judgmentCriteria.setCourtType(courtCriteria.getCourtType());

	    judgmentCriteria.setCcCourtId(courtCriteria.getCcCourtId());
	    judgmentCriteria.setCcCourtDivisionId(courtCriteria.getCcCourtDivisionId());
		
		judgmentCriteria.setScJudgmentForm(judgmentCriteriaForm.getScJudgmentForm());

    	judgmentCriteria.setKeywords(judgmentCriteriaForm.getKeywords());
	    
	    judgmentCriteria.setScPersonnelType(judgmentCriteriaForm.getScPersonnelType());
	    judgmentCriteria.setScCourtChamberId(courtCriteria.getScCourtChamberId());
	    judgmentCriteria.setScCourtChamberDivisionId(courtCriteria.getScCourtChamberDivisionId());
	    
	    judgmentCriteria.setCtDissentingOpinion(judgmentCriteriaForm.getCtDissentingOpinion());
	    
    	judgmentCriteria.setJudgmentTypes(Lists.newArrayList(judgmentCriteriaForm.getJudgmentTypes()));
	    
	    judgmentCriteria.setLegalBase(judgmentCriteriaForm.getLegalBase());
		judgmentCriteria.setReferencedRegulation(judgmentCriteriaForm.getReferencedRegulation());
		judgmentCriteria.setLawJournalEntryId(judgmentCriteriaForm.getLawJournalEntryId());
		judgmentCriteria.setReferencedCourtCaseId(judgmentCriteriaForm.getReferencedCourtCaseId());
	    
		return judgmentCriteria;
	}
	
}

