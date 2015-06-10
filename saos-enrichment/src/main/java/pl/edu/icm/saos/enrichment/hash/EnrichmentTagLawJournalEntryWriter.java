package pl.edu.icm.saos.enrichment.hash;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;

/**
 * Spring batch writer of law journal entries obtained from enrichment tags
 * 
 * @author madryk
 */
@Service
public class EnrichmentTagLawJournalEntryWriter implements ItemWriter<LawJournalEntry> {

    private LawJournalEntryRepository lawJournalEntryRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void write(List<? extends LawJournalEntry> lawJournalEntries) throws Exception {
        lawJournalEntryRepository.save(lawJournalEntries);
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setLawJournalEntryRepository(LawJournalEntryRepository lawJournalEntryRepository) {
        this.lawJournalEntryRepository = lawJournalEntryRepository;
    }

    
}
