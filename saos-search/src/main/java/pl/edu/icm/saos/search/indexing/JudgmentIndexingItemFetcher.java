package pl.edu.icm.saos.search.indexing;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Fetcher of {@link JudgmentIndexingItem}s
 * 
 * @author madryk
 */
@Service
public class JudgmentIndexingItemFetcher {
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns list of {@link JudgmentIndexingItem} for judgments that need to be indexed
     */
    @Transactional
    public List<JudgmentIndexingItem> fetchJudgmentIndexingItems() {
        
        List<Long> notIndexedIds = judgmentRepository.findAllNotIndexedIds();
        
        Map<Long, Long> referencingJudgmentsCountInfo = countReferencingJudgmentsForNotIndexed();
        
        List<JudgmentIndexingItem> judgmentsIndexingItems = Lists.newLinkedList();
        for (Long notIndexedId : notIndexedIds) {
            long referencingCount = (referencingJudgmentsCountInfo.containsKey(notIndexedId)) ? referencingJudgmentsCountInfo.get(notIndexedId) : 0;
            
            judgmentsIndexingItems.add(new JudgmentIndexingItem(notIndexedId, referencingCount));
        }
        
        return judgmentsIndexingItems;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    /**
     * Counts number of referencing judgments for not indexed judgments.
     * When judgment is not referenced by any other judgment then there
     * will be no result for it in returned map.
     * 
     * @return map containing pairs of judgmentId and number of
     *     judgments referencing to judgment with that id
     */
    private Map<Long, Long> countReferencingJudgmentsForNotIndexed() {
        Query query = entityManager.createNativeQuery("SELECT refId\\:\\:text\\:\\:bigint, count(*) "
                + " FROM enrichment_tag tag "
                + " JOIN json_array_elements(tag.value) tagValue ON true "
                + " JOIN json_array_elements(tagValue->'judgmentIds') refId ON true "
                + " WHERE tag.tag_type = '" + EnrichmentTagTypes.REFERENCED_COURT_CASES + "' "
                + " GROUP BY refId\\:\\:text\\:\\:bigint;");
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        
        Map<Long, Long> idsWithReferencingCount = Maps.newHashMap();
        for (Object[] result : results) {
            idsWithReferencingCount.put(((BigInteger)result[0]).longValue(), ((BigInteger)result[1]).longValue());
        }
        
        return idsWithReferencingCount;
    }
}
