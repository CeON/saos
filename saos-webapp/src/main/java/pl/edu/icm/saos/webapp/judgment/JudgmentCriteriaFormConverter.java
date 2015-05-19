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
        
        convertCourtCriteria(judgmentCriteriaForm, judgmentCriteria);
        
        judgmentCriteria.setScJudgmentFormId(judgmentCriteriaForm.getScJudgmentFormId());

        judgmentCriteria.setKeywords(judgmentCriteriaForm.getKeywords());
        
        judgmentCriteria.setScPersonnelType(judgmentCriteriaForm.getScPersonnelType());
        
        judgmentCriteria.setCtDissentingOpinion(judgmentCriteriaForm.getCtDissentingOpinion());
        
        judgmentCriteria.setJudgmentTypes(Lists.newArrayList(judgmentCriteriaForm.getJudgmentTypes()));
        
        judgmentCriteria.setLegalBase(judgmentCriteriaForm.getLegalBase());
        judgmentCriteria.setReferencedRegulation(judgmentCriteriaForm.getReferencedRegulation());
        judgmentCriteria.setLawJournalEntryCode(judgmentCriteriaForm.getLawJournalEntryCode());
        judgmentCriteria.setReferencedCourtCaseId(judgmentCriteriaForm.getReferencedCourtCaseId());
        
        return judgmentCriteria;
    }

    
    //------------------------ PRIVATE --------------------------
    
    private void convertCourtCriteria(JudgmentCriteriaForm judgmentCriteriaForm, JudgmentCriteria judgmentCriteria) {
        
        CourtCriteria courtCriteria = judgmentCriteriaForm.getCourtCriteria();
        
        judgmentCriteria.setCourtType(courtCriteria.getCourtType());
        
        if (courtCriteria.isCcIncludeDependentCourtJudgments()) {
            judgmentCriteria.setCcDirectOrSuperiorCourtId(courtCriteria.getCcCourtId());
        } else {
            judgmentCriteria.setCcCourtId(courtCriteria.getCcCourtId());
        }
        
        judgmentCriteria.setScCourtChamberId(courtCriteria.getScCourtChamberId());
        judgmentCriteria.setScCourtChamberDivisionId(courtCriteria.getScCourtChamberDivisionId());
        judgmentCriteria.setCcCourtDivisionId(courtCriteria.getCcCourtDivisionId());
        
    }
    
}

