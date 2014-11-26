package pl.edu.icm.saos.search.indexing;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * Spring Batch writer for reseting indexed flag in {@link Judgment judgments}
 * 
 * @author madryk
 */
@Service
public class JudgmentResetIndexFlagWriter implements ItemWriter<Judgment> {

    private JudgmentRepository judgmentRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void write(List<? extends Judgment> items) throws Exception {
        judgmentRepository.save(items);
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

}