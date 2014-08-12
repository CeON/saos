package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import java.util.List;

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

    //private static Logger log = LoggerFactory.getLogger(CcjImportProcessWriter.class);
    
    private JudgmentRepository judgmentRepository;
    
    @Override
    public void write(List<? extends CommonCourtJudgment> judgments) throws Exception {
        judgmentRepository.save(judgments);
        judgmentRepository.flush();
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

}
