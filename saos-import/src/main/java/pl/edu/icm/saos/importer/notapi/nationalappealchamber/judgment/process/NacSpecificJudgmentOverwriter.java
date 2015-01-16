package pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.process;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;

/**
 * @author madryk
 */
@Service("nacSpecificJudgmentOverwriter")
public class NacSpecificJudgmentOverwriter implements JudgmentOverwriter<NationalAppealChamberJudgment> {

    @Override
    public void overwriteJudgment(NationalAppealChamberJudgment oldJudgment,
            NationalAppealChamberJudgment newJudgment,
            ImportCorrectionList correctionList) {
        
    }

}
