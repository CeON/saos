package pl.edu.icm.saos.importer.commoncourt.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("lawJournalEntryCreator")
class LawJournalEntryCreator {
    
    
    private LawJournalEntryRepository lawJournalEntryRepository;
    
    
    /**
     * Returns {@link LawJournalEntry} equivalent to {@link LawJournalEntryData} from the data repository
     * ({@link LawJournalEntryRepository#findOneByYearAndJournalNoAndEntry(int, int, int)}). <br/>
     * If there is no such a {@link LawJournalEntry} in the repository yet, then first it is created and then returned.
     */
    public LawJournalEntry getOrCreateLawJournalEntry(LawJournalEntryData entryData) {
        LawJournalEntry lawJournalEntry = lawJournalEntryRepository.findOneByYearAndJournalNoAndEntry(entryData.getYear(), entryData.getJournalNo(), entryData.getEntry());
        if (lawJournalEntry == null) {
            lawJournalEntry = new LawJournalEntry(entryData.getYear(), entryData.getJournalNo(), entryData.getEntry(), entryData.getTitle());
            lawJournalEntryRepository.save(lawJournalEntry);
            lawJournalEntryRepository.flush();
            
        }
        return lawJournalEntry;
    }


    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setLawJournalEntryRepository(LawJournalEntryRepository lawJournalEntryRepository) {
        this.lawJournalEntryRepository = lawJournalEntryRepository;
    }


   
}
