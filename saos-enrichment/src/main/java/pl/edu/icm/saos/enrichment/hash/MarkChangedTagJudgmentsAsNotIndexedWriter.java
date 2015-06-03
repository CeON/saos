package pl.edu.icm.saos.enrichment.hash;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * String batch writer for step responsible for
 * marking judgments with changed enrichment tags as not indexed
 *  
 * @author madryk
 */
@Service
public class MarkChangedTagJudgmentsAsNotIndexedWriter implements ItemWriter<Long> {

    private JudgmentRepository judgmentRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void write(List<? extends Long> judgmentsIds) throws Exception {
        
        judgmentRepository.markAsNotIndexed(Lists.newLinkedList(judgmentsIds));
        
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }
    
    

}
