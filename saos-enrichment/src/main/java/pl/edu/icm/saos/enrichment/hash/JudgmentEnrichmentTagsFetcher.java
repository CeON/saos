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
 * Fetcher of enrichment tags related to judgment (pointing to judgment
 * with {@link EnrichmentTag#getJudgmentId()} or {@link EnrichmentTag#getValue()})
 * @author madryk
 */
@Service
public class JudgmentEnrichmentTagsFetcher {
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns enrichment tags related to judgments that are specified in method parameter.
     * If some judgment doesn't have any related enrichment tags then in results it will
     * be presented as {@link JudgmentEnrichmentTags} with empty {@link JudgmentEnrichmentTags#getEnrichmentTags()}
     * list.
     */
    public List<JudgmentEnrichmentTags> fetchEnrichmentTagsForJudgments(List<Long> judgmentIds) {
        
        Map<Long, JudgmentEnrichmentTags> judgmentsDirectEnrichmentTagsMap = fetchJudgmentsDirectTags(judgmentIds);
        
        Map<Long, JudgmentEnrichmentTags> judgmentsReferencingEnrichmentTags = fetchJudgmentsReferencingTags(judgmentIds);
        
        
        Map<Long, JudgmentEnrichmentTags> judgmentsEnrichmentTagsMap = initializeJudgmentsEnrichmentTagsMap(judgmentIds);
        
        
        mergeJudgmentsEnrichmentTagsMaps(judgmentsDirectEnrichmentTagsMap, judgmentsEnrichmentTagsMap);
        mergeJudgmentsEnrichmentTagsMaps(judgmentsReferencingEnrichmentTags, judgmentsEnrichmentTagsMap);
        
        return Lists.newLinkedList(judgmentsEnrichmentTagsMap.values());
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    /**
     * Returns direct enrichment tags of judgments ({@link EnrichmentTag#getJudgmentId()} equal to judgment id).
     * Returned result is in form of a map where keys are equal to {@link JudgmentEnrichmentTags#getJudgmentId()}.
     * If some judgment doesn't have any direct enrichment tags then it will be omitted in returned map.
     */
    private Map<Long, JudgmentEnrichmentTags> fetchJudgmentsDirectTags(List<Long> judgmentIds) {
        List<EnrichmentTag> enrichmentTags = enrichmentTagRepository.findAllByJudgmentIds(judgmentIds);
        
        Map<Long, JudgmentEnrichmentTags> judgmentsEnrichmentTagsMap = Maps.newHashMap();
        enrichmentTags.forEach(tag -> putEnrichmentTagInMap(judgmentsEnrichmentTagsMap, tag.getJudgmentId(), tag));
        
        return judgmentsEnrichmentTagsMap;
    }
    
    /**
     * Returns referencing enrichment tags of judgments ({@link EnrichmentTag#getValue()} contains
     * reference to judgment id).
     * Returned result is in form of a map where keys are equal to {@link JudgmentEnrichmentTags#getJudgmentId()}.
     * If some judgment doesn't have any referencing enrichment tags then it will be omitted in returned map.
     */
    private Map<Long, JudgmentEnrichmentTags> fetchJudgmentsReferencingTags(List<Long> judgmentIds) {
        String select = "SELECT refId\\:\\:text\\:\\:bigint refId, tag.judgment_id, tag.tag_type, tag.value\\:\\:text"
                + " FROM enrichment_tag tag"
                + " JOIN json_array_elements(tag.value) tagValue ON true"
                + " JOIN json_array_elements(tagValue->'judgmentIds') refId ON true"
                + " WHERE tag.tag_type = 'REFERENCED_COURT_CASES' AND refId\\:\\:text\\:\\:bigint in (:judgmentIds);";
        Query query = entityManager.createNativeQuery(select);
        query.setParameter("judgmentIds", judgmentIds);
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        
        Map<Long, JudgmentEnrichmentTags> judgmentsEnrichmentTagsMap = Maps.newHashMap();
        
        for (Object[] result : results) {
            long referencedJudgmentId = ((BigInteger)result[0]).longValue();
            long judgmentId = ((BigInteger)result[1]).longValue();
            String tagType = (String)result[2];
            String value = (String)result[3];
            
            EnrichmentTag tag = new EnrichmentTag();
            tag.setJudgmentId(judgmentId);
            tag.setTagType(tagType);
            tag.setValue(value);
            
            putEnrichmentTagInMap(judgmentsEnrichmentTagsMap, referencedJudgmentId, tag);
        }
        
        return judgmentsEnrichmentTagsMap;
    }
    
    /**
     * Returns map of {@link JudgmentEnrichmentTags} with empty {@link JudgmentEnrichmentTags#getEnrichmentTags()}
     * for all judgments passed as method parameter.
     */
    private Map<Long, JudgmentEnrichmentTags> initializeJudgmentsEnrichmentTagsMap(List<Long> judgmentIds) {
        Map<Long, JudgmentEnrichmentTags> judgmentsEnrichmentTagsMap = Maps.newHashMapWithExpectedSize(judgmentIds.size());
        
        for (long judgmentId : judgmentIds) {
            judgmentsEnrichmentTagsMap.put(judgmentId, new JudgmentEnrichmentTags(judgmentId));
        }
        
        return judgmentsEnrichmentTagsMap;
    }
    
    private void mergeJudgmentsEnrichmentTagsMaps(Map<Long, JudgmentEnrichmentTags> source, Map<Long, JudgmentEnrichmentTags> target) {
        source.forEach((key, value) -> putJudgmentEnrichmentTagsInMap(target, value));
    }
    
    private void putEnrichmentTagInMap(Map<Long, JudgmentEnrichmentTags> judgmentsEnrichmentTagsMap, long relatedJudgmentId, EnrichmentTag enrichmentTag) {
        if (!judgmentsEnrichmentTagsMap.containsKey(relatedJudgmentId)) {
            judgmentsEnrichmentTagsMap.put(relatedJudgmentId, new JudgmentEnrichmentTags(relatedJudgmentId));
        }
        judgmentsEnrichmentTagsMap.get(relatedJudgmentId).addEnrichmentTag(enrichmentTag);
    }
    
    private void putJudgmentEnrichmentTagsInMap(Map<Long, JudgmentEnrichmentTags> judgmentsEnrichmentTagsMap, JudgmentEnrichmentTags judgmentEnrichmentTags) {
        if (!judgmentsEnrichmentTagsMap.containsKey(judgmentEnrichmentTags.getJudgmentId())) {
            judgmentsEnrichmentTagsMap.put(judgmentEnrichmentTags.getJudgmentId(), judgmentEnrichmentTags);
        } else {
            judgmentsEnrichmentTagsMap.get(judgmentEnrichmentTags.getJudgmentId()).addAllEnrichmentTags(judgmentEnrichmentTags.getEnrichmentTags());
        }
    }
    
    
}
