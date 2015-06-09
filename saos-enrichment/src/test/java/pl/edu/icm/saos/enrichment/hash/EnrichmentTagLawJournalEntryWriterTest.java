package pl.edu.icm.saos.enrichment.hash;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class EnrichmentTagLawJournalEntryWriterTest {

    @InjectMocks
    private EnrichmentTagLawJournalEntryWriter enrichmentTagLawJournalEntryWriter = new EnrichmentTagLawJournalEntryWriter();
    
    @Mock
    private LawJournalEntryRepository lawJournalEntryRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void write() throws Exception {
        
        // given
        LawJournalEntry lawJournalEntry1 = new LawJournalEntry(1999, 201, 505, "Ustawa 1");
        LawJournalEntry lawJournalEntry2 = new LawJournalEntry(2000, 205, 510, "Ustawa 2");
        
        // execute
        enrichmentTagLawJournalEntryWriter.write(Lists.newArrayList(lawJournalEntry1, lawJournalEntry2));
        
        // assert
        verify(lawJournalEntryRepository).save(Lists.newArrayList(lawJournalEntry1, lawJournalEntry2));
    }
}
