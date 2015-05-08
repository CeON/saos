package pl.edu.icm.saos.enrichment.delete;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.enrichment.reference.JudgmentReferenceRemover;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
@Service
public class JudgmentWithEnrichmentDeleter {

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    @Autowired
    @Qualifier("judgmentReferenceRemover")
    private JudgmentReferenceRemover judgmentReferenceRemover;
    
    
    //------------------------ LOGIC --------------------------
    
    @Transactional
    public void delete(List<Long> judgmentIds) {
        judgmentRepository.delete(judgmentIds);
        deleteEnrichmentTags(judgmentIds);
        judgmentReferenceRemover.removeReference(judgmentIds);
    }
    
    @Transactional
    public void delete(Judgment judgment) {
        judgmentRepository.delete(judgment);
        deleteEnrichmentTags(Lists.newArrayList(judgment.getId()));
        judgmentReferenceRemover.removeReference(Lists.newArrayList(judgment.getId()));
    }
    
    @Transactional
    public void delete(Long judgmentId) {
        judgmentRepository.delete(judgmentId);
        deleteEnrichmentTags(Lists.newArrayList(judgmentId));
        judgmentReferenceRemover.removeReference(Lists.newArrayList(judgmentId));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void deleteEnrichmentTags(List<Long> judgmentIds) {
        Query q = entityManager.createQuery("delete from " + EnrichmentTag.class.getName() + " tag where tag.judgmentId in (:judgmentIds)").setParameter("judgmentIds", judgmentIds);
        q.executeUpdate();
    }    
    
}
