package pl.edu.icm.saos.webapp.judgment;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
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

	    if (judgmentCriteriaForm.getKeywords() == null) {
	    	judgmentCriteria.setKeywords(Lists.newLinkedList());
	    } else {
	    	judgmentCriteria.setKeywords(judgmentCriteriaForm.getKeywords());
	    }
	    
	    judgmentCriteria.setScPersonnelType(judgmentCriteriaForm.getScPersonnelType());
	    judgmentCriteria.setScCourtChamberId(judgmentCriteriaForm.getSupremeChamberId());
	    judgmentCriteria.setScCourtChamberDivisionId(judgmentCriteriaForm.getSupremeChamberDivisionId());
		
	    judgmentCriteria.setJudgmentTypes(convertJudgmentTypes(judgmentCriteriaForm.getJudgmentTypes()));
	    
	    judgmentCriteria.setLegalBase(judgmentCriteriaForm.getLegalBase());
		judgmentCriteria.setReferencedRegulation(judgmentCriteriaForm.getReferencedRegulation());
	    
		return judgmentCriteria;
	}

	
	//------------------------ PRIVATE --------------------------
	
	/*
	 *  Converts list of strings to list of {@link JudgmentType} and returns it.
	 */
	private List<JudgmentType> convertJudgmentTypes(List<String> judgmentTypes) {
		
		if (judgmentTypes == null) {
			return Lists.newArrayList();
		} else {
			return judgmentTypes.stream().map(j -> JudgmentType.valueOf(j)).collect(Collectors.toList());
		}
	}
	
}

