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

import pl.edu.icm.saos.persistence.model.LawJournalEntry;

import com.google.common.collect.Lists;

/**
 * Spring batch reader of law journal entries obtained from enrichment tags
 * 
 * @author madryk
 */
@Service
public class EnrichmentTagLawJournalEntryReader implements ItemStreamReader<LawJournalEntry> {

    private EnrichmentTagLawJournalEntryFetcher enrichmentTagLawJournalEntryFetcher;
    
    
    private LinkedList<LawJournalEntry> lawJournalEntries = Lists.newLinkedList();
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        
        lawJournalEntries = Lists.newLinkedList(
                enrichmentTagLawJournalEntryFetcher.fetchTagLawJournalEntriesWihoutCorrespondingEntryInDb());
        
    }
    
    @Override
    public LawJournalEntry read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        
        return lawJournalEntries.poll();
        
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        
    }

    @Override
    public void close() throws ItemStreamException {
        
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setEnrichmentTagLawJournalEntryFetcher(
            EnrichmentTagLawJournalEntryFetcher enrichmentTagLawJournalEntryFetcher) {
        this.enrichmentTagLawJournalEntryFetcher = enrichmentTagLawJournalEntryFetcher;
    }

}
