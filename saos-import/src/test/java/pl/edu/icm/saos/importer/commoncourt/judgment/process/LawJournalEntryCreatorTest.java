package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class LawJournalEntryCreatorTest {

    
    private LawJournalEntryCreator lawJournalEntryCreator = new LawJournalEntryCreator();
    
    
    private LawJournalEntryRepository lawJournalEntryRepository = mock(LawJournalEntryRepository.class);
    
    private LawJournalEntry lawJournalEntry = new LawJournalEntry();
    
    
    @Before
    public void before() {
        
        Whitebox.setInternalState(lawJournalEntry, "id", 114);
        lawJournalEntry.setYear(2011);
        lawJournalEntry.setEntry(11);
        lawJournalEntry.setEntry(1);
        lawJournalEntry.setTitle("Title title");
        
        when(lawJournalEntryRepository.findOneByYearAndEntry(Mockito.eq(lawJournalEntry.getYear()), Mockito.eq(lawJournalEntry.getEntry()))).thenReturn(lawJournalEntry);
        
        lawJournalEntryCreator.setLawJournalEntryRepository(lawJournalEntryRepository);
        
        
    }

    
    //------------------------ TESTS --------------------------
    
    @Test
    public void testGetOrCreateLawJournalEntry_Get() {
        // given
        LawJournalEntryData entryData = new LawJournalEntryData(lawJournalEntry.getYear(), lawJournalEntry.getJournalNo(), lawJournalEntry.getEntry(), "Tiisiss");
        
        // execute
        LawJournalEntry foundLawJournalEntry = lawJournalEntryCreator.getOrCreateLawJournalEntry(entryData);
        
        // assert
        verify(lawJournalEntryRepository, never()).save(Mockito.any(LawJournalEntry.class));
        assertEquals(lawJournalEntry, foundLawJournalEntry);
        assertTrue(lawJournalEntry == foundLawJournalEntry);
        
               
    }
    

    @Test
    public void testGetOrCreateLawJournalEntry_Create() {
        // given
        int entry = lawJournalEntry.getEntry() + 2;
        String title = "Tiiiis";
        LawJournalEntryData entryData = new LawJournalEntryData(lawJournalEntry.getYear(), lawJournalEntry.getJournalNo(), entry, title);
        
        // execute
        LawJournalEntry foundLawJournalEntry = lawJournalEntryCreator.getOrCreateLawJournalEntry(entryData);
        
        // assert
        verify(lawJournalEntryRepository).save(Mockito.any(LawJournalEntry.class));
        verify(lawJournalEntryRepository).findOneByYearAndEntry(lawJournalEntry.getYear(), entry);
        
        assertEquals(title, foundLawJournalEntry.getTitle());
        assertEquals(lawJournalEntry.getYear(), foundLawJournalEntry.getYear());
        assertEquals(lawJournalEntry.getJournalNo(), foundLawJournalEntry.getJournalNo());
        assertEquals(entry, foundLawJournalEntry.getEntry());
        
        assertFalse(lawJournalEntry == foundLawJournalEntry);
        
    }
    
}
