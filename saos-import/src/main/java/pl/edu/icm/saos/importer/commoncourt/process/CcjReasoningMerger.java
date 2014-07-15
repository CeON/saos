package pl.edu.icm.saos.importer.commoncourt.process;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.OverwriterUtils;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjReasoningMerger")
class CcjReasoningMerger {

     
    /**
     * {@link SourceCode#COMMON_COURT} judgment source sometimes keeps judgment and reasons for judgment as
     * separate judgment entities. This method (used by import process) merges them into one judgment. 
     * @param judgment judgment that will be updated
     * @param reasoning holds the reasoning data that the judgment will be updated with
     */
    void mergeReasoning(CommonCourtJudgment judgment, CommonCourtJudgment reasoningJudgment) {

        OverwriterUtils.overwriteReasoning(judgment, reasoningJudgment);
        
        mergeLegalBases(judgment, reasoningJudgment);
        
        mergeReferencedRegulations(judgment, reasoningJudgment);
        
        mergeKeywords(judgment, reasoningJudgment);
    }




        
    //------------------------ PRIVATE --------------------------

    
    private void mergeKeywords(CommonCourtJudgment judgment, CommonCourtJudgment reasoningJudgment) {
        for (CcJudgmentKeyword ccJudgmentKeyword : reasoningJudgment.getKeywords()) {
            if (!judgment.containsKeyword(ccJudgmentKeyword)) {
                judgment.addKeyword(ccJudgmentKeyword);
            }
        }
    }


    private void mergeReferencedRegulations(CommonCourtJudgment judgment, CommonCourtJudgment reasoningJudgment) {
        for (JudgmentReferencedRegulation referencedRegulation : reasoningJudgment.getReferencedRegulations()) {
            if (!judgment.containsReferencedRegulation(referencedRegulation)) {
                JudgmentReferencedRegulation regulation = new JudgmentReferencedRegulation();
                regulation.setLawJournalEntry(referencedRegulation.getLawJournalEntry());
                regulation.setRawText(referencedRegulation.getRawText());
                judgment.addReferencedRegulation(regulation);
            }
        }
    }

    
    private void mergeLegalBases(CommonCourtJudgment judgment, CommonCourtJudgment reasoningJudgment) {
        for (String legalBase : reasoningJudgment.getLegalBases()) {
            if (!judgment.containsLegalBase(legalBase)) {
                judgment.addLegalBase(legalBase);
            }
        }
    }





    
}
