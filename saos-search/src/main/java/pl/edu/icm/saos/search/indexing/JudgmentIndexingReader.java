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

import com.google.common.collect.Lists;

/**
 * Spring batch reader of judgments for indexing
 * @author madryk
 */
@Service
public class JudgmentIndexingReader implements ItemStreamReader<JudgmentIndexingData> {
    
    private JudgmentEnrichmentService judgmentEnrichmentService;
    
    private JudgmentIndexingItemFetcher judgmentIndexingItemFetcher;
    
    
    private volatile Queue<JudgmentIndexingItem> judgmentIndexingItems = Lists.newLinkedList();
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        
        judgmentIndexingItems = new ConcurrentLinkedQueue<JudgmentIndexingItem>(
                judgmentIndexingItemFetcher.fetchJudgmentIndexingItems());
        
    }

    @Override
    public JudgmentIndexingData read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        
        JudgmentIndexingItem judgmentIndexingItem = judgmentIndexingItems.poll();
        
        if (judgmentIndexingItem == null) {
            return null;
        }
        
        Judgment judgment = judgmentEnrichmentService.findOneAndEnrich(judgmentIndexingItem.getJudgmentId());
        if (judgment == null) {
            return null;
        }
        
        JudgmentIndexingData judgmentIndexingData = new JudgmentIndexingData();
        
        judgmentIndexingData.setJudgment(judgment);
        judgmentIndexingData.setReferencingCount(judgmentIndexingItem.getReferencingCount());
        
        return judgmentIndexingData;
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
    public void setJudgmentEnrichmentService(JudgmentEnrichmentService judgmentEnrichmentService) {
        this.judgmentEnrichmentService = judgmentEnrichmentService;
    }

    @Autowired
    public void setJudgmentIndexingItemFetcher(JudgmentIndexingItemFetcher judgmentIndexingItemFetcher) {
        this.judgmentIndexingItemFetcher = judgmentIndexingItemFetcher;
    }

}
