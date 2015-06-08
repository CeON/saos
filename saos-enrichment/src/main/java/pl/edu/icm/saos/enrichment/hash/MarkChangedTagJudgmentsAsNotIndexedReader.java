package pl.edu.icm.saos.enrichment.hash;

import java.util.LinkedList;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.enrichment.JudgmentEnrichmentHashRepository;

/**
 * Spring batch reader for step responsible for
 * marking judgments with changed enrichment tags as not indexed
 * 
 * @author madryk
 */
@Service
public class MarkChangedTagJudgmentsAsNotIndexedReader implements ItemStreamReader<Long> {

    private JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository;
    
    
    private LinkedList<Long> judgmentIds = Lists.newLinkedList();
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        
        judgmentIds = Lists.newLinkedList(
                judgmentEnrichmentHashRepository.findAllJudgmentsIdsToProcess());
    }

    @Override
    public Long read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        
        return judgmentIds.poll();
        
    }
    
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        
    }

    @Override
    public void close() throws ItemStreamException {
        
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentEnrichmentHashRepository(JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository) {
        this.judgmentEnrichmentHashRepository = judgmentEnrichmentHashRepository;
    }

}
