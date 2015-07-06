package pl.edu.icm.saos.enrichment.hash;

import java.util.LinkedList;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@Service
public class UpdateEnrichmentHashReader implements ItemStreamReader<JudgmentEnrichmentTags> {

    private JudgmentRepository judgmentRepository;
    
    private JudgmentEnrichmentTagsFetcher judgmentEnrichmentTagsFetcher;
    
    
    private int judgmentsEnrichmentTagsPageSize = 5000;
    
    private LinkedList<Long> judgmentIds = Lists.newLinkedList();
    
    private LinkedList<JudgmentEnrichmentTags> judgmentsEnrichmentTags = Lists.newLinkedList();
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        
        judgmentIds = Lists.newLinkedList(
                judgmentRepository.findAllIds());
        
    }

    @Override
    public JudgmentEnrichmentTags read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        
        JudgmentEnrichmentTags judgmentEnrichmentTags = judgmentsEnrichmentTags.poll();
        
        
        if (judgmentEnrichmentTags == null && judgmentIds.isEmpty()) {
            return null;
        }
        
        if (judgmentEnrichmentTags == null) {
            
            fetchPageOfJudgmentsEnrichmentTags();
            
            return read();
        }
        
        
        return judgmentEnrichmentTags;
    }
    
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        
    }

    @Override
    public void close() throws ItemStreamException {
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void fetchPageOfJudgmentsEnrichmentTags() {
        List<Long> judgmentIdsPage = Lists.newLinkedList();
        
        while(!judgmentIds.isEmpty() && judgmentIdsPage.size() < judgmentsEnrichmentTagsPageSize) {
            judgmentIdsPage.add(judgmentIds.removeFirst());
        }
        
        judgmentsEnrichmentTags = Lists.newLinkedList(
                judgmentEnrichmentTagsFetcher.fetchEnrichmentTagsForJudgments(judgmentIdsPage));
    }
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    @Autowired
    public void setJudgmentEnrichmentTagsFetcher(JudgmentEnrichmentTagsFetcher judgmentsToRecalculateHashFetcher) {
        this.judgmentEnrichmentTagsFetcher = judgmentsToRecalculateHashFetcher;
    }

    @Value("${enrichment.hash.calculate.page.size:5000}")
    public void setJudgmentsEnrichmentTagsPageSize(int judgmentsEnrichmentTagsPageSize) {
        this.judgmentsEnrichmentTagsPageSize = judgmentsEnrichmentTagsPageSize;
    }

}
