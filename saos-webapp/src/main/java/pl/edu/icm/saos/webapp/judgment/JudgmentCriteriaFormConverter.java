package pl.edu.icm.saos.webapp.judgment;


import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
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
		judgmentCriteria.setCourtType(judgmentCriteriaForm.getCourtType());

	    judgmentCriteria.setCcCourtId(judgmentCriteriaForm.getCommonCourtId());
	    judgmentCriteria.setCcCourtDivisionId(judgmentCriteriaForm.getCommonCourtDivisionId());
		
		judgmentCriteria.setScJudgmentForm(judgmentCriteriaForm.getScJudgmentForm());

    	judgmentCriteria.setKeywords(judgmentCriteriaForm.getKeywords());
	    
	    judgmentCriteria.setScPersonnelType(judgmentCriteriaForm.getScPersonnelType());
	    judgmentCriteria.setScCourtChamberId(judgmentCriteriaForm.getSupremeChamberId());
	    judgmentCriteria.setScCourtChamberDivisionId(judgmentCriteriaForm.getSupremeChamberDivisionId());
	    
    	judgmentCriteria.setJudgmentTypes(Lists.newArrayList(judgmentCriteriaForm.getJudgmentTypes()));
	    
	    judgmentCriteria.setLegalBase(judgmentCriteriaForm.getLegalBase());
		judgmentCriteria.setReferencedRegulation(judgmentCriteriaForm.getReferencedRegulation());
	    
		return judgmentCriteria;
	}
	
}

