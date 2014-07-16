package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjImportProcessWriter")
public class CcjImportProcessWriter implements ItemWriter<CommonCourtJudgment> {

    private static Logger log = LoggerFactory.getLogger(CcjImportProcessWriter.class);
    
    private JudgmentRepository judgmentRepository;
    
    @Override
    public void write(List<? extends CommonCourtJudgment> judgments) throws Exception {
        log.debug("====== writing: {} ", judgments);
        if (judgments.size()>1 && judgments.get(0).getCaseNumber().contains("5")) {
            throw new IllegalStateException("incorrect argument in writing ");
        }
        judgmentRepository.save(judgments);
        judgmentRepository.flush();
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

}
