package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.AbstractJudgmentOverwriter;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccJudgmentOverwriter")
class CcJudgmentOverwriter extends AbstractJudgmentOverwriter<CommonCourtJudgment> {

    @Override
    protected void overwriteSpecificData(CommonCourtJudgment oldJudgment, CommonCourtJudgment newJudgment) {
        overwriteCourt(oldJudgment, newJudgment);
        
        overwriteKeywords(oldJudgment, newJudgment);
    }


  
    //------------------------ PRIVATE --------------------------
    
    private void overwriteCourt(CommonCourtJudgment oldJudgment, CommonCourtJudgment newJudgment) {
        oldJudgment.setCourtDivision(newJudgment.getCourtDivision());
    }

    
    private void overwriteKeywords(CommonCourtJudgment oldJudgment, CommonCourtJudgment newJudgment) {
        for (CcJudgmentKeyword oldKeyword : oldJudgment.getKeywords()) {
            if (!newJudgment.containsKeyword(oldKeyword)) {
                oldJudgment.removeKeyword(oldKeyword);
            }
        }
        for (CcJudgmentKeyword keyword : newJudgment.getKeywords()) {
            if (!oldJudgment.containsKeyword(keyword)) {
                oldJudgment.addKeyword(keyword);
            }
        }
    }

  
}
