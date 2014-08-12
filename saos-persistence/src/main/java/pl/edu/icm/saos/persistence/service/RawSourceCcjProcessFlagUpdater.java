package pl.edu.icm.saos.persistence.service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.ImportProcessingStatus;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Service("rawSourceCcjProcessFlagUpdater")
public class RawSourceCcjProcessFlagUpdater {
    
    /**
     * Marks all {@link RawSourceCcJudgment}s with the {@link RawSourceCcJudgment#getProcessingStatus()} set
     * to {@link ImportProcessingStatus#OK} or {@link ImportProcessingStatus#ERROR} as processed 
     * ({@link RawSourceCcJudgment#isProcessed()})
     */
    @Autowired
    private EntityManager entityManager;
    
    @Transactional
    public void markProcessedAllEligible() {
        String q = "update " + RawSourceCcJudgment.class.getName() + " set processed = true " +
                    " where processingStatus in (:processingStatuses) and processed = false";
        Query query = entityManager.createQuery(q).setParameter("processingStatuses", Lists.newArrayList(ImportProcessingStatus.OK));
        query.executeUpdate();
    }
}
