package pl.edu.icm.saos.persistence.repository;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author Łukasz Dumiszewski
 */

public interface LawJournalEntryRepositoryCustom {

    
    public LawJournalEntry findOneByYearAndJournalNoAndEntry(int year, int journalNo, int entry);
    
}
