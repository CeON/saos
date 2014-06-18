package pl.edu.icm.saos.importer.commoncourt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class LawJournalExtractorTest {

    
    @Test
    public void extractLawJournal_OK() {
        String lawJournalEntryString = "Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych (Dz. U. z 1997 r. Nr 133, poz. 884 - art. 11 a; art. 11 a ust. 1; art. 14; art. 14 ust. 6; art. 14 ust. 7)";
        LawJournalEntryData entryData = LawJournalEntryExtractor.extractLawJournalEntry(lawJournalEntryString);
        assertEquals("Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych", entryData.getTitle());
        assertEquals(1997, entryData.getYear());
        assertEquals(133, entryData.getJournalNo());
        assertEquals(884, entryData.getEntry());
    }
    
    @Test
    public void extractLawJournal_OK2() {
        String lawJournalEntryString = "Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych (dz.U. z 1997 r.nr133, poz884 - art. 11 a; art. 11 a ust. 1; art. 14; art. 14 ust. 6; art. 14 ust. 7)";
        LawJournalEntryData entryData = LawJournalEntryExtractor.extractLawJournalEntry(lawJournalEntryString);
        assertEquals("Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych", entryData.getTitle());
        assertEquals(1997, entryData.getYear());
        assertEquals(133, entryData.getJournalNo());
        assertEquals(884, entryData.getEntry());
    }
    
    
    @Test
    public void extractLawJournal_NoDzU() {
        String lawJournalEntryString = "Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych z 1997 r.nr133, poz884 - art. 11 a; art. 11 a ust. 1; art. 14; art. 14 ust. 6; art. 14 ust. 7)";
        LawJournalEntryData entryData = LawJournalEntryExtractor.extractLawJournalEntry(lawJournalEntryString);
        assertNull(entryData);
    }

    
    @Test
    public void extractLawJournal_NoYear() {
        String lawJournalEntryString = "Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych (dz.U. z 1997 nr133, poz884 - art. 11 a; art. 11 a ust. 1; art. 14; art. 14 ust. 6; art. 14 ust. 7)";
        LawJournalEntryData entryData = LawJournalEntryExtractor.extractLawJournalEntry(lawJournalEntryString);
        assertNull(entryData);
    }
    
    @Test
    public void extractLawJournal_NoNumber() {
        String lawJournalEntryString = "Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych (dz.U. z 1997 r.n133, poz884 - art. 11 a; art. 11 a ust. 1; art. 14; art. 14 ust. 6; art. 14 ust. 7)";
        LawJournalEntryData entryData = LawJournalEntryExtractor.extractLawJournalEntry(lawJournalEntryString);
        assertNull(entryData);
    }
    
    @Test
    public void extractLawJournal_NoEntry() {
        String lawJournalEntryString = "Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych (dz.U. z 1997 r.nr133, po884 - art. 11 a; art. 11 a ust. 1; art. 14; art. 14 ust. 6; art. 14 ust. 7)";
        LawJournalEntryData entryData = LawJournalEntryExtractor.extractLawJournalEntry(lawJournalEntryString);
        assertNull(entryData);
    }
    
    @Test
    public void extractLawJournal_NoTitle() {
        String lawJournalEntryString = "(dz.U. z 1997 r.nr133, poz884 - art. 11 a; art. 11 a ust. 1; art. 14; art. 14 ust. 6; art. 14 ust. 7)";
        LawJournalEntryData entryData = LawJournalEntryExtractor.extractLawJournalEntry(lawJournalEntryString);
        assertEquals("", entryData.getTitle());
        assertEquals(1997, entryData.getYear());
        assertEquals(133, entryData.getJournalNo());
        assertEquals(884, entryData.getEntry());
    }

}
