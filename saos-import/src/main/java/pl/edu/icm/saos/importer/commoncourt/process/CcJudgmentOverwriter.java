package pl.edu.icm.saos.importer.commoncourt.process;

import pl.edu.icm.saos.importer.common.AbstractJudgmentOverwriter;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtData;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class CcJudgmentOverwriter extends AbstractJudgmentOverwriter<CommonCourtJudgment> {

    @Override
    protected void overwriteSpecificData(CommonCourtJudgment oldJudgment, CommonCourtJudgment newJudgment) {
        overwriteCourtData(oldJudgment, newJudgment);
        
        overwriteKeywords(oldJudgment, newJudgment);
    }


  
    //------------------------ PRIVATE --------------------------
    
    private void overwriteCourtData(CommonCourtJudgment oldJudgment, CommonCourtJudgment newJudgment) {
        CommonCourtData oldCourtData = oldJudgment.getCourtData();
        CommonCourtData newCourtData = newJudgment.getCourtData();
        if (newCourtData.getCourt() == null) {
            oldJudgment.getCourtData().setCourt(null);
        } else {
            if (oldCourtData.getCourt() == null) {
                oldCourtData.setCourt(new CommonCourt());
            }
            oldCourtData.getCourt().setAppealCourtCode(newCourtData.getCourt().getAppealCourtCode());
            oldCourtData.getCourt().setRegionalCourtCode(newCourtData.getCourt().getRegionalCourtCode());
            oldCourtData.getCourt().setDistrictCourtCode(newCourtData.getCourt().getDistrictCourtCode());
            oldCourtData.getCourt().setName(newCourtData.getCourt().getName());
            oldCourtData.getCourt().setShortName(newCourtData.getCourt().getShortName());
        }
        oldCourtData.setDivisionNumber(newCourtData.getDivisionNumber());
        oldCourtData.setDivisionType(newCourtData.getDivisionType());
    }

    
    private void overwriteKeywords(CommonCourtJudgment oldJudgment, CommonCourtJudgment newJudgment) {
        oldJudgment.removeAllKeywords();
        for (CcJudgmentKeyword keyword : newJudgment.getKeywords()) {
            oldJudgment.addKeyword(keyword);
        }
    }

  
}
