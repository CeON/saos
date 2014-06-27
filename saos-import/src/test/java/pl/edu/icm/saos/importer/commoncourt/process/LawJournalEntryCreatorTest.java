package pl.edu.icm.saos.importer.commoncourt.process;

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

import pl.edu.icm.saos.importer.commoncourt.process.LawJournalEntryCreator;
import pl.edu.icm.saos.importer.commoncourt.process.LawJournalEntryData;
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
        
        lawJournalEntry.setId(114);
        lawJournalEntry.setYear(2011);
        lawJournalEntry.setEntry(11);
        lawJournalEntry.setEntry(1);
        lawJournalEntry.setTitle("Title title");
        
        when(lawJournalEntryRepository.findOneByYearAndJournalNoAndEntry(Mockito.eq(lawJournalEntry.getYear()), Mockito.eq(lawJournalEntry.getJournalNo()), Mockito.eq(lawJournalEntry.getEntry()))).thenReturn(lawJournalEntry);
        
        lawJournalEntryCreator.setLawJournalEntryRepository(lawJournalEntryRepository);
        
        
    }

    
    @Test
    public void testGetOrCreateLawJournalEntry_Get() {
        
        
        LawJournalEntryData entryData = new LawJournalEntryData(lawJournalEntry.getYear(), lawJournalEntry.getJournalNo(), lawJournalEntry.getEntry(), "Tiisiss");
        LawJournalEntry foundLawJournalEntry = lawJournalEntryCreator.getOrCreateLawJournalEntry(entryData);
        
        verify(lawJournalEntryRepository, never()).save(Mockito.any(LawJournalEntry.class));
        assertEquals(lawJournalEntry, foundLawJournalEntry);
        assertTrue(lawJournalEntry == foundLawJournalEntry);
        
               
    }
    

    @Test
    public void testGetOrCreateLawJournalEntry_Create() {
        int journalNo = lawJournalEntry.getJournalNo() + 2;
        String title = "Tiiiis";
        LawJournalEntryData entryData = new LawJournalEntryData(lawJournalEntry.getYear(), journalNo, lawJournalEntry.getEntry(), title);
        LawJournalEntry foundLawJournalEntry = lawJournalEntryCreator.getOrCreateLawJournalEntry(entryData);
        
        verify(lawJournalEntryRepository).save(Mockito.any(LawJournalEntry.class));
        verify(lawJournalEntryRepository).findOneByYearAndJournalNoAndEntry(lawJournalEntry.getYear(), journalNo, lawJournalEntry.getEntry());
        
        assertEquals(title, foundLawJournalEntry.getTitle());
        assertEquals(lawJournalEntry.getYear(), foundLawJournalEntry.getYear());
        assertEquals(journalNo, foundLawJournalEntry.getJournalNo());
        assertEquals(lawJournalEntry.getEntry(), foundLawJournalEntry.getEntry());
        
        assertFalse(lawJournalEntry == foundLawJournalEntry);
        
    }
    
}
