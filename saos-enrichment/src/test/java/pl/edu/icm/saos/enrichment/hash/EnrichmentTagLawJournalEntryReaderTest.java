package pl.edu.icm.saos.enrichment.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.item.ExecutionContext;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class EnrichmentTagLawJournalEntryReaderTest {

    @InjectMocks
    private EnrichmentTagLawJournalEntryReader enrichmentTagLawJournalEntryReader = new EnrichmentTagLawJournalEntryReader();
    
    @Mock
    private EnrichmentTagLawJournalEntryFetcher enrichmentTagLawJournalEntryFetcher;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void read() throws Exception {
        
        // given
        LawJournalEntry lawJournalEntry1 = new LawJournalEntry(2001, 24, 321, "Ustawa 1");
        LawJournalEntry lawJournalEntry2 = new LawJournalEntry(2003, 27, 345, "Ustawa 2");
        when(enrichmentTagLawJournalEntryFetcher.fetchTagLawJournalEntriesWihoutCorrespondingEntryInDb())
            .thenReturn(Lists.newArrayList(lawJournalEntry1, lawJournalEntry2));
        enrichmentTagLawJournalEntryReader.open(Mockito.mock(ExecutionContext.class));
        
        // execute
        LawJournalEntry retLawJournalEntry1 = enrichmentTagLawJournalEntryReader.read();
        LawJournalEntry retLawJournalEntry2 = enrichmentTagLawJournalEntryReader.read();
        LawJournalEntry retLawJournalEntry3 = enrichmentTagLawJournalEntryReader.read();
        
        // assert
        assertEquals(lawJournalEntry1, retLawJournalEntry1);
        assertEquals(lawJournalEntry2, retLawJournalEntry2);
        assertNull(retLawJournalEntry3);
    }
    
}
