package pl.edu.icm.saos.webapp.judgment;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

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
		judgmentCriteria.setDateFrom(judgmentCriteriaForm.getDateFrom());
		judgmentCriteria.setDateTo(judgmentCriteriaForm.getDateTo());
		
		judgmentCriteria.setJudgeName(judgmentCriteriaForm.getJudgeName());
		judgmentCriteria.setLegalBase(judgmentCriteriaForm.getLegalBase());
		judgmentCriteria.setReferencedRegulation(judgmentCriteriaForm.getReferencedRegulation());		
		judgmentCriteria.setCourtType(judgmentCriteriaForm.getCourtType());

	    judgmentCriteria.setCcCourtId(judgmentCriteriaForm.getCommonCourtId());
	    judgmentCriteria.setCcCourtDivisionId(judgmentCriteriaForm.getCommonCourtDivisionId());
		judgmentCriteria.addKeyword(judgmentCriteriaForm.getKeyword());
	    
	    judgmentCriteria.setScCourtChamberId(judgmentCriteriaForm.getSupremeChamberId());
	    judgmentCriteria.setScCourtChamberDivisionId(judgmentCriteriaForm.getSupremeChamberDivisionId());
		
		return judgmentCriteria;
	}
	
}

