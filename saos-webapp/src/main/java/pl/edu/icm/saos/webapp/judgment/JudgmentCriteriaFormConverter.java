package pl.edu.icm.saos.webapp.judgment;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

/**
 * @author Łukasz Pawełczak
 *
 */
@Service
public class JudgmentCriteriaFormConverter {

	public JudgmentCriteria convert(JudgmentCriteriaForm element) {
		JudgmentCriteria judgmentCriteria = new JudgmentCriteria();
		
		judgmentCriteria.setAll(element.getAll());
		judgmentCriteria.setCaseNumber(element.getSignature());
		try {
		    judgmentCriteria.setCourtId(Integer.valueOf(element.getCourtId()));
		} catch (NumberFormatException e) { }
		judgmentCriteria.setCourtName(element.getCourtName());
		judgmentCriteria.setJudgeName(element.getJudgeName());
		judgmentCriteria.setKeyword(element.getKeyword());
		judgmentCriteria.setLegalBase(element.getLegalBase());
		judgmentCriteria.setReferencedRegulation(element.getReferencedRegulation());

		judgmentCriteria.setDateFrom(element.getDateFrom());
		judgmentCriteria.setDateTo(element.getDateTo());
		
		
		return judgmentCriteria;
	}
	
	
}
