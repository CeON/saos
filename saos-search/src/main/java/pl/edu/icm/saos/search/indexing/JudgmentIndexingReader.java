package pl.edu.icm.saos.search.indexing;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentService;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * Spring batch reader of judgments for indexing
 * @author madryk
 */
@Service
public class JudgmentIndexingReader implements ItemStreamReader<Judgment> {

    private JudgmentRepository judgmentRepository;
    
    private JudgmentEnrichmentService judgmentEnrichmentService;
    
    
    private volatile Queue<Long> judgmentIds = Lists.newLinkedList();
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        
        judgmentIds = new ConcurrentLinkedQueue<Long>(judgmentRepository.findAllNotIndexedIds());
    
    }

    @Override
    public Judgment read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        
        Long id = judgmentIds.poll();
        
        if (id == null) {
            return null;
        }
        
        return judgmentEnrichmentService.findOneAndEnrich(id);
    }
    
    @Override
    public void update(ExecutionContext executionContext)
            throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    @Autowired
    public void setJudgmentEnrichmentService(JudgmentEnrichmentService judgmentEnrichmentService) {
        this.judgmentEnrichmentService = judgmentEnrichmentService;
    }

}
