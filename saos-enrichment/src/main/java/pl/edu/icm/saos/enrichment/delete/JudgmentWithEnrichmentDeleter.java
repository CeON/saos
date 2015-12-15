package pl.edu.icm.saos.enrichment.delete;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.enrichment.reference.TagJudgmentReferenceRemover;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * Deleter of {@link Judgments} with corresponding {@link EnrichmentTag}s and
 * references to judgment existing in {@link EnrichmentTag#getValue()}
 * 
 * @author madryk
 */
@Service
public class JudgmentWithEnrichmentDeleter {

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    @Qualifier("tagJudgmentReferenceRemover")
    private TagJudgmentReferenceRemover tagJudgmentReferenceRemover;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Deletes {@link Judgment}s with the given judgmentIds, corresponding
     * {@link EnrichmentTag}s and references to this judgments from {@link EnrichmentTag#getValue()}<br/>
     * 
     * See {@link JudgmentRepository#delete(List)} for deletion of {@link Judgment}s only
     */
    @Transactional
    public void delete(List<Long> judgmentIds) {
        judgmentRepository.delete(judgmentIds);
        deleteEnrichmentTags(judgmentIds);
        tagJudgmentReferenceRemover.removeReferences(judgmentIds);
    }
    
    /**
     * Deletes the given {@link Judgment}, corresponding
     * {@link EnrichmentTag}s and references to this judgment from {@link EnrichmentTag#getValue()}<br/>
     * 
     * See {@link JudgmentRepository#delete(Judgment)} for deletion of {@link Judgment} only
     */
    @Transactional
    public void delete(Judgment judgment) {
        judgmentRepository.delete(judgment);
        deleteEnrichmentTags(Lists.newArrayList(judgment.getId()));
        tagJudgmentReferenceRemover.removeReferences(Lists.newArrayList(judgment.getId()));
    }
    
    /**
     * Deletes a {@link Judgment} with the given judgmentId, corresponding
     * {@link EnrichmentTag}s and references to this judgment from {@link EnrichmentTag#getValue()}<br/>
     * 
     * See {@link JudgmentRepository#delete(Long)} for deletion of {@link Judgment} only
     */
    @Transactional
    public void delete(Long judgmentId) {
        judgmentRepository.delete(judgmentId);
        deleteEnrichmentTags(Lists.newArrayList(judgmentId));
        tagJudgmentReferenceRemover.removeReferences(Lists.newArrayList(judgmentId));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void deleteEnrichmentTags(List<Long> judgmentIds) {
        Query q = entityManager.createQuery("delete from " + EnrichmentTag.class.getName() + " tag where tag.judgmentId in (:judgmentIds)").setParameter("judgmentIds", judgmentIds);
        q.executeUpdate();
    }    
    
}
