package pl.edu.icm.saos.persistence.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author madryk
 */
@Service
public class LawJournalEntryCodeExtractor {
    
    
    //------------------------ LOGIC --------------------------
    
    public int extractYear(String lawJournalEntryCode) {
        return extractEntryCodePart(lawJournalEntryCode, 0);
    }
    
    public int extractJournalNo(String lawJournalEntryCode) {
        return extractEntryCodePart(lawJournalEntryCode, 1);
    }
    
    public int extractEntry(String lawJournalEntryCode) {
        return extractEntryCodePart(lawJournalEntryCode, 2);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private int extractEntryCodePart(String lawJournalEntryCode, int partNumber) {
        return Integer.parseInt(StringUtils.split(lawJournalEntryCode, LawJournalEntry.ENTRY_CODE_PARTS_SEPARATOR)[partNumber]);
    }
}
