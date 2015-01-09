package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
@Service
class CtObjectDeleter {

    private static Logger log = LoggerFactory.getLogger(CtObjectDeleter.class);
    
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Deletes all {@link ConstitutionalTribunalJudgment}s that do not have corresponding {@link RawSourceCtJudgment}s. 
     */
    @Transactional
    public void deleteJudgmentsWithoutRawSourceCtJudgment() {
        
        log.debug("Deleting ctJudgments without corresponding rawSourceCtJudgments");
        
        String q = "select ctJudgment.id from " + ConstitutionalTribunalJudgment.class.getName() + " ctJudgment " +
                " where not exists  (select rJudgment from "+ RawSourceCtJudgment.class.getName() + " rJudgment " +
                                        " where rJudgment.sourceId = ctJudgment.sourceInfo.sourceJudgmentId)";
        
        @SuppressWarnings("unchecked")
        List<Integer> judgmentIds = entityManager.createQuery(q).getResultList();

        if (!judgmentIds.isEmpty()) {
            judgmentRepository.delete(judgmentIds);
        }
    }
}
