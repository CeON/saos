package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scjImportProcessWriter")
public class ScjImportProcessWriter implements ItemWriter<SupremeCourtJudgment> {

    private JudgmentRepository judgmentRepository;
    
    
    @Override
    public void write(List<? extends SupremeCourtJudgment> judgments) {
        judgmentRepository.save(judgments);
        
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

}
