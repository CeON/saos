package pl.edu.icm.saos.enrichment.hash;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.enrichment.EnrichmentTestSupport;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class EnrichmentTagLawJournalEntryFetcherTest extends EnrichmentTestSupport {

    @Autowired
    private EnrichmentTagLawJournalEntryFetcher enrichmentTagLawJournalEntryFetcher;
    
    @Autowired
    private LawJournalEntryRepository lawJournalEntryRepository;
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void fetchTagLawJournalEntriesWihoutCorrespondingEntryInDb() {
        
        // given
        LawJournalEntry dbLawJournalEntry = new LawJournalEntry(2000, 10, 100, "Ustawa 1");
        LawJournalEntry tagLawJournalEntry1 = new LawJournalEntry(2003, 13, 203, "Ustawa 2");
        LawJournalEntry tagLawJournalEntry2 = new LawJournalEntry(2004, 24, 204, "Ustawa 3");
        LawJournalEntry tagLawJournalEntry3 = new LawJournalEntry(2000, 0, 100, "Ustawa 1"); // same as dbLawJournalEntry but with different journalNo
        LawJournalEntry tagLawJournalEntry4 = new LawJournalEntry(2003, 0, 203, "Ustawa 2"); // same as tagLawJournalEntry1 but with different journalNo
        
        lawJournalEntryRepository.save(dbLawJournalEntry);
        testPersistenceObjectFactory.createReferencedRegulationsTag(3L, "prefix_", dbLawJournalEntry, tagLawJournalEntry1);
        testPersistenceObjectFactory.createReferencedRegulationsTag(4L, "prefix_", tagLawJournalEntry1);
        testPersistenceObjectFactory.createReferencedRegulationsTag(5L, "prefix_", tagLawJournalEntry1, tagLawJournalEntry2);
        testPersistenceObjectFactory.createReferencedRegulationsTag(6L, "prefix_", tagLawJournalEntry3, tagLawJournalEntry4);
        
        
        // execute
        List<LawJournalEntry> retLawJournalEntries = enrichmentTagLawJournalEntryFetcher.fetchTagLawJournalEntriesWihoutCorrespondingEntryInDb();
        
        
        // assert
        assertThat(retLawJournalEntries, containsInAnyOrder(tagLawJournalEntry1, tagLawJournalEntry2));
        
    }
}
