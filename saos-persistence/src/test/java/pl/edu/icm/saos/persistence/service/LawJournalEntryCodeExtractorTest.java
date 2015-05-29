package pl.edu.icm.saos.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author madryk
 */
public class LawJournalEntryCodeExtractorTest {

    private LawJournalEntryCodeExtractor lawJournalEntryCodeExtractor = new LawJournalEntryCodeExtractor();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void isCorrectLawJournalEntryCode_CORRECT() {
        // given
        String lawJournalEntryCode = "1964/43/296";
        
        // execute & assert
        assertTrue(lawJournalEntryCodeExtractor.isCorrectLawJournalEntryCode(lawJournalEntryCode));
    }
    
    @Test
    public void isCorrectLawJournalEntryCode_NULL_ENTRY_CODE() {
        // execute & assert
        assertFalse(lawJournalEntryCodeExtractor.isCorrectLawJournalEntryCode(null));
    }
    
    @Test
    public void isCorrectLawJournalEntryCode_INVALID_ENTRY() {
        // given
        String lawJournalEntryCode = "1964/43/2a6";
        
        // execute & assert
        assertFalse(lawJournalEntryCodeExtractor.isCorrectLawJournalEntryCode(lawJournalEntryCode));
    }
    
    @Test
    public void isCorrectLawJournalEntryCode_EMPTY_JOURNAL_NO() {
        // given
        String lawJournalEntryCode = "1964//296";
        
        // execute & assert
        assertFalse(lawJournalEntryCodeExtractor.isCorrectLawJournalEntryCode(lawJournalEntryCode));
    }
    
    @Test
    public void isCorrectLawJournalEntryCode_MISSING_ENTRY() {
        // given
        String lawJournalEntryCode = "1964/43";
        
        // execute & assert
        assertFalse(lawJournalEntryCodeExtractor.isCorrectLawJournalEntryCode(lawJournalEntryCode));
    }
    
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
    
    @Test(expected = IllegalArgumentException.class)
    public void extractEntry_INVALID() {
        // given
        String lawJournalEntryCode = "1964/43/abc";
        
        // execute
        lawJournalEntryCodeExtractor.extractEntry(lawJournalEntryCode);
    }
}
