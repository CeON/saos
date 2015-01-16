package pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.process;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.notapi.common.JudgmentObjectDeleter;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceNacJudgment;

/**
 * @author madryk
 */
@Service
public class NacjImportProcessStepExecutionListener implements StepExecutionListener {

    @Autowired
    private JudgmentObjectDeleter judgmentObjectDeleter;


    //------------------------ LOGIC --------------------------

    @Override
    public void beforeStep(StepExecution stepExecution) {

        judgmentObjectDeleter.deleteJudgmentsWithoutRawSourceJudgment(NationalAppealChamberJudgment.class, RawSourceNacJudgment.class);

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

}
