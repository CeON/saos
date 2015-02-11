package pl.edu.icm.saos.webapp.lawjournal;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author madryk
 */
@Service
public class SimpleLawJournalEntryConverter {

    
    //------------------------ LOGIC --------------------------
    
    public List<SimpleLawJournalEntry> convertLawJournalEntries(List<LawJournalEntry> lawJournalEntries) {
        return lawJournalEntries.stream()
                .map(lawJournalEntry -> convertLawJournalEntry(lawJournalEntry))
                .collect(Collectors.toList());
    }
    
    
    public SimpleLawJournalEntry convertLawJournalEntry(LawJournalEntry lawJournalEntry) {
        return new SimpleLawJournalEntry(
                lawJournalEntry.getId(),
                lawJournalEntry.getYear(),
                lawJournalEntry.getJournalNo(),
                lawJournalEntry.getEntry(),
                lawJournalEntry.getTitle());
    }

}
