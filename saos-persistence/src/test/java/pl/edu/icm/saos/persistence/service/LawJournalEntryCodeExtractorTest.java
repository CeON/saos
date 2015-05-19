package pl.edu.icm.saos.persistence.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author madryk
 */
public class LawJournalEntryCodeExtractorTest {

    private LawJournalEntryCodeExtractor lawJournalEntryCodeExtractor = new LawJournalEntryCodeExtractor();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void extractYear() {
        // given
        String lawJournalEntryCode = "1964/43/296";
        
        // execute & assert
        assertEquals(1964, lawJournalEntryCodeExtractor.extractYear(lawJournalEntryCode));
    }
    
    @Test
    public void extractJournalNo() {
        // given
        String lawJournalEntryCode = "1964/43/296";
        
        // execute & assert
        assertEquals(43, lawJournalEntryCodeExtractor.extractJournalNo(lawJournalEntryCode));
    }
    
    @Test
    public void extractJournalNo_PRECEDING_ZERO() {
        // given
        String lawJournalEntryCode = "1964/0043/296";
        
        // execute & assert
        assertEquals(43, lawJournalEntryCodeExtractor.extractJournalNo(lawJournalEntryCode));
    }
    
    @Test
    public void extractEntry() {
        // given
        String lawJournalEntryCode = "1964/43/296";
        
        // execute & assert
        assertEquals(296, lawJournalEntryCodeExtractor.extractEntry(lawJournalEntryCode));
    }
    
    @Test
    public void extractEntry_PRECEDING_ZERO() {
        // given
        String lawJournalEntryCode = "1964/43/0296";
        
        // execute & assert
        assertEquals(296, lawJournalEntryCodeExtractor.extractEntry(lawJournalEntryCode));
    }
}
