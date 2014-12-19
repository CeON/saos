package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;

/**
 * @author madryk
 */
@Service("ctSpecificJudgmentOverwriter")
public class CtSpecificJudgmentOverwriter implements JudgmentOverwriter<ConstitutionalTribunalJudgment> {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void overwriteJudgment(ConstitutionalTribunalJudgment oldJudgment,
            ConstitutionalTribunalJudgment newJudgment, ImportCorrectionList correctionList) {

        overwriteDissentingOpinions(oldJudgment, newJudgment);

    }

    
    //------------------------ PRIVATE --------------------------
    
    private void overwriteDissentingOpinions(ConstitutionalTribunalJudgment oldJudgment,
            ConstitutionalTribunalJudgment newJudgment) {
        
        oldJudgment.getDissentingOpinions().stream().filter(opinion -> !newJudgment.containsDissentingOpinion(opinion))
                .forEach(opinion -> oldJudgment.removeDissentingOpinion(opinion));
        
        newJudgment.getDissentingOpinions().stream().filter(opinion -> !oldJudgment.containsDissentingOpinion(opinion))
                .forEach(opinion -> oldJudgment.addDissentingOpinion(opinion));
    }
}
