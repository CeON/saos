package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class LawJournalEntryRepositoryTest extends PersistenceTestSupport {

    @Autowired
    private LawJournalEntryRepository lawJournalEntryRepository;
    
    
    @Test
    public void testSave() {
        Assert.assertEquals(0, lawJournalEntryRepository.count());
        
        createTestLawJournalEntry();
        
        Assert.assertEquals(1, lawJournalEntryRepository.count());
        
    }

    @Test
    public void testFindByYearAndJournalNoAndEntry() {
        Assert.assertEquals(0, lawJournalEntryRepository.count());
        
        LawJournalEntry lawJournalEntry = createTestLawJournalEntry();
        
        LawJournalEntry dbLawJournalEntry = lawJournalEntryRepository.findOneByYearAndJournalNoAndEntry(lawJournalEntry.getYear()+1, lawJournalEntry.getJournalNo(), lawJournalEntry.getEntry());
        assertNull(dbLawJournalEntry);
        
        dbLawJournalEntry = lawJournalEntryRepository.findOneByYearAndJournalNoAndEntry(lawJournalEntry.getYear(), lawJournalEntry.getJournalNo(), lawJournalEntry.getEntry());
        assertNotNull(dbLawJournalEntry);
        assertEquals(lawJournalEntry, dbLawJournalEntry);
        
        
    }

    
    //------------------------ PRIVATE --------------------------

    private LawJournalEntry createTestLawJournalEntry() {
        LawJournalEntry lawJournalEntry = new LawJournalEntry();
        lawJournalEntry.setTitle("Prawo o ooooo...");
        lawJournalEntry.setYear(2011);
        lawJournalEntry.setJournalNo(11);
        lawJournalEntry.setEntry(2);
        lawJournalEntryRepository.save(lawJournalEntry);
        return lawJournalEntry;
    }
    
}
