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
	
	public JudgmentCriteria convert(JudgmentCriteriaForm element) {
		JudgmentCriteria judgmentCriteria = new JudgmentCriteria();
		
		judgmentCriteria.setAll(element.getAll());
		judgmentCriteria.setCaseNumber(element.getSignature());
		judgmentCriteria.setDateFrom(element.getDateFrom());
		judgmentCriteria.setDateTo(element.getDateTo());
		
		judgmentCriteria.setJudgeName(element.getJudgeName());
		judgmentCriteria.setLegalBase(element.getLegalBase());
		judgmentCriteria.setReferencedRegulation(element.getReferencedRegulation());		
		judgmentCriteria.setCourtType(element.getCourtType());

		
		try {
		    judgmentCriteria.setCcCourtId(Integer.valueOf(element.getCommonCourtId()));
		} catch (NumberFormatException e) { }
		
		judgmentCriteria.setCcCourtName(element.getCommonCourtName());
		
		try {
		    judgmentCriteria.setScCourtChamberId(Integer.valueOf(element.getSupremeChamberId()));
		} catch (NumberFormatException e) { }
		
		try {
		    judgmentCriteria.setScCourtChamberDivisionId(Integer.valueOf(element.getSupremeChamberDivisionId()));
		} catch (NumberFormatException e) { }
		
		judgmentCriteria.setKeyword(element.getKeyword());
		
		return judgmentCriteria;
	}
	
}

