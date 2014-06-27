package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author Łukasz Dumiszewski
 */

public interface LawJournalEntryRepository extends JpaRepository<LawJournalEntry, Integer>, LawJournalEntryRepositoryCustom {

    
    
}
