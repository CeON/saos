package pl.edu.icm.saos.enrichment.hash;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author madryk
 */
@Service
public class JudgmentEnrichmentTagsFetcher {
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    public List<JudgmentEnrichmentTags> fetchEnrichmentTagsForJudgments(List<Long> judgmentIds) {
        
        List<EnrichmentTag> enrichmentTags = enrichmentTagRepository.findAllByJudgmentIds(judgmentIds);
        
        List<Object[]> enrichmentTagsWithReference = fetchJudgmentsReferencingTags(judgmentIds);
        
        Map<Long, JudgmentEnrichmentTags> judgmentsEnrichmentTagsMap = Maps.newHashMapWithExpectedSize(judgmentIds.size());
        
        initializeJudgmentsEnrichmentTagsMap(judgmentsEnrichmentTagsMap, judgmentIds);
        collectJudgmentsEnrichmentTags(judgmentsEnrichmentTagsMap, enrichmentTags);
        collectJudgmentsReferencingEnrichmentTags(judgmentsEnrichmentTagsMap, enrichmentTagsWithReference);
        
        return Lists.newLinkedList(judgmentsEnrichmentTagsMap.values());
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private List<Object[]> fetchJudgmentsReferencingTags(List<Long> judgmentIds) {
        String select = "SELECT refId\\:\\:text\\:\\:bigint refId, tag.judgment_id, tag.tag_type, tag.value\\:\\:text"
                + " FROM enrichment_tag tag"
                + " JOIN json_array_elements(tag.value) tagValue ON true"
                + " JOIN json_array_elements(tagValue->'judgmentIds') refId ON true"
                + " WHERE tag.tag_type = 'REFERENCED_COURT_CASES' AND refId\\:\\:text\\:\\:bigint in (:judgmentIds);";
        Query query = entityManager.createNativeQuery(select);
        query.setParameter("judgmentIds", judgmentIds);
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        
        return results;
    }
    
    private void initializeJudgmentsEnrichmentTagsMap(Map<Long, JudgmentEnrichmentTags> judgmentsEnrichmentTagsMap, List<Long> judgmentIds) {
        for (long judgmentId : judgmentIds) {
            judgmentsEnrichmentTagsMap.put(judgmentId, new JudgmentEnrichmentTags(judgmentId));
        }
    }
    
    private void collectJudgmentsEnrichmentTags(Map<Long, JudgmentEnrichmentTags> judgmentsEnrichmentTagsMap, List<EnrichmentTag> enrichmentTags) {
        for (EnrichmentTag enrichmentTag : enrichmentTags) {
            judgmentsEnrichmentTagsMap.get(enrichmentTag.getJudgmentId()).addEnrichmentTag(enrichmentTag);
        }
    }
    
    private void collectJudgmentsReferencingEnrichmentTags(Map<Long, JudgmentEnrichmentTags> judgmentsEnrichmentTagsMap, List<Object[]> enrichmentTagsWithReference) {
        for (Object[] enrichmentTagWithReference : enrichmentTagsWithReference) {
            long referencedJudgmentId = ((BigInteger)enrichmentTagWithReference[0]).longValue();
            long judgmentId = ((BigInteger)enrichmentTagWithReference[1]).longValue();
            String tagType = (String)enrichmentTagWithReference[2];
            String value = (String)enrichmentTagWithReference[3];
            
            EnrichmentTag tag = new EnrichmentTag();
            tag.setJudgmentId(judgmentId);
            tag.setTagType(tagType);
            tag.setValue(value);
            
            judgmentsEnrichmentTagsMap.get(referencedJudgmentId).addEnrichmentTag(tag);
        }
    }
    
    
}
