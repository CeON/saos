package pl.edu.icm.saos.webapp.lawjournal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class SimpleLawJournalEntryConverterTest {

    private SimpleLawJournalEntryConverter simpleLawJournalEntryConverter = new SimpleLawJournalEntryConverter();
    
    
    private LawJournalEntry firstLawJournalEntry;
    private LawJournalEntry secondLawJournalEntry;
    
    private SimpleLawJournalEntry firstSimpleLawJournalEntry;
    private SimpleLawJournalEntry secondSimpleLawJournalEntry;
    
    
    @Before
    public void setUp() {
        firstLawJournalEntry = new LawJournalEntry(1999, 123, 321, "some title");
        Whitebox.setInternalState(firstLawJournalEntry, "id", 1);
        
        secondLawJournalEntry = new LawJournalEntry(2000, 124, 421, "some other title");
        Whitebox.setInternalState(secondLawJournalEntry, "id", 2);
        
        
        firstSimpleLawJournalEntry = new SimpleLawJournalEntry(1, 1999, 123, 321, "some title");
        secondSimpleLawJournalEntry = new SimpleLawJournalEntry(2, 2000, 124, 421, "some other title");
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void convertLawJournalEntry() {
        
        // execute
        SimpleLawJournalEntry actual = simpleLawJournalEntryConverter.convertLawJournalEntry(firstLawJournalEntry);
        
        // assert
        assertEquals(firstSimpleLawJournalEntry, actual);
        
    }
    
    @Test
    public void convertLawJournalEntries() {
        
        // given
        List<LawJournalEntry> lawJournalEntries = Lists.newArrayList(firstLawJournalEntry, secondLawJournalEntry);
        
        // execute
        List<SimpleLawJournalEntry> actual = simpleLawJournalEntryConverter.convertLawJournalEntries(lawJournalEntries);
        
        // assert
        assertThat(actual, contains(firstSimpleLawJournalEntry, secondSimpleLawJournalEntry));
        
    }
}
