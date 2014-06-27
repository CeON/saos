package pl.edu.icm.saos.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.ReferencedRegulation;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;

/**
 * @author Łukasz Dumiszewski
 */

@Service
public class DbCleaner {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional
    public void clean() {
        deleteAll(ReferencedRegulation.class);
        deleteAll(LawJournalEntry.class);
        deleteAll(Judgment.class);
        deleteAll(CommonCourt.class);
        deleteAll(CommonCourtDivisionType.class);
        
        deleteAll(RawSourceCcJudgment.class);
        
    }
    
    
    private void deleteAll(Class<?> clazz) {
        Query query = entityManager.createQuery("delete from " + clazz.getName());
        query.executeUpdate();
    }
    
}
