package pl.edu.icm.saos.enrichment.hash;

import static org.mockito.Mockito.inOrder;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class EnrichmentTagLawJournalEntryWriterTest {

    @InjectMocks
    private EnrichmentTagLawJournalEntryWriter enrichmentTagLawJournalEntryWriter = new EnrichmentTagLawJournalEntryWriter();
    
    @Mock
    private LawJournalEntryRepository lawJournalEntryRepository;
    
    @Mock
    private EntityManager entityManager;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void write() throws Exception {
        
        // given
        LawJournalEntry lawJournalEntry1 = new LawJournalEntry(1999, 201, 505, "Ustawa 1");
        LawJournalEntry lawJournalEntry2 = new LawJournalEntry(2000, 205, 510, "Ustawa 2");
        
        // execute
        enrichmentTagLawJournalEntryWriter.write(Lists.newArrayList(lawJournalEntry1, lawJournalEntry2));
        
        // assert
        InOrder inOrder = inOrder(lawJournalEntryRepository, entityManager);
        inOrder.verify(lawJournalEntryRepository).save(Lists.newArrayList(lawJournalEntry1, lawJournalEntry2));
        inOrder.verify(lawJournalEntryRepository).flush();
        inOrder.verify(entityManager).clear();
        
    }
}
