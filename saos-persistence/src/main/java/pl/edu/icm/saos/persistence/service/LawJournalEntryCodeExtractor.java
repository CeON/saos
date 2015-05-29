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
    
    public boolean isCorrectLawJournalEntryCode(String lawJournalEntryCode) {
        if (lawJournalEntryCode == null) {
            return false;
        }
        
        String[] entryCodeParts = StringUtils.split(lawJournalEntryCode, LawJournalEntry.ENTRY_CODE_PARTS_SEPARATOR);
        
        if (entryCodeParts.length != 3) {
            return false;
        }
        
        return entryCodeParts[0].matches("\\d+") && entryCodeParts[1].matches("\\d+") && entryCodeParts[2].matches("\\d+");
    }
    
    public int extractYear(String lawJournalEntryCode) {
        if (!isCorrectLawJournalEntryCode(lawJournalEntryCode)) {
            throw new IllegalArgumentException(lawJournalEntryCode + " is invalid law journal entry code");
        }
        
        return extractEntryCodePart(lawJournalEntryCode, 0);
    }
    
    public int extractJournalNo(String lawJournalEntryCode) {
        if (!isCorrectLawJournalEntryCode(lawJournalEntryCode)) {
            throw new IllegalArgumentException(lawJournalEntryCode + " is invalid law journal entry code");
        }
        
        return extractEntryCodePart(lawJournalEntryCode, 1);
    }
    
    public int extractEntry(String lawJournalEntryCode) {
        if (!isCorrectLawJournalEntryCode(lawJournalEntryCode)) {
            throw new IllegalArgumentException(lawJournalEntryCode + " is invalid law journal entry code");
        }
        
        return extractEntryCodePart(lawJournalEntryCode, 2);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private int extractEntryCodePart(String lawJournalEntryCode, int partNumber) {
        return Integer.parseInt(StringUtils.split(lawJournalEntryCode, LawJournalEntry.ENTRY_CODE_PARTS_SEPARATOR)[partNumber]);
    }
}
