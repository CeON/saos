package pl.edu.icm.saos.enrichment.reference;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.json.JsonStringWriter;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * Class for removing reference to judgment from {@link EnrichmentTag}.
 * It supports tags where reference removing is based on modifying {@link EnrichmentTag#getValue()}.
 * 
 * @author madryk
 * @param <TAG_TYPE> - type of enrichment tag value type
 */
public abstract class TagModifyingJudgmentReferenceRemover<TAG_TYPE> implements JudgmentReferenceRemover {
    
    protected final static String JUDGMENT_IDS_QUERY_PARAM_NAME = "judgmentIds";
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private JsonStringParser<TAG_TYPE> jsonStringParser;
    
    @Autowired
    private JsonStringWriter<TAG_TYPE> jsonStringWriter;

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void removeReference(List<Long> judgmentIds) {
        
        List<EnrichmentTag> results = findTagsWithReferences(judgmentIds);
        
        if (!results.isEmpty()) {
            removeReferencesFromTags(results, judgmentIds);
            
            enrichmentTagRepository.save(results);
            
            List<Long> judgmentsToReindex = results.stream().map(t -> t.getJudgmentId()).collect(Collectors.toList());
            judgmentRepository.markAsNotindexed(judgmentsToReindex);
            
        }
        
    }
    
    /**
     * Builds query for selecting enrichment tags which reference to judgment need to be removed
     */
    protected abstract String buildSelectQuery();
    
    /**
     * Removes reference to judgment from enrichment tag value
     */
    protected abstract void removeReference(TAG_TYPE tagValue, List<Long> judgmentId);
    
    
    //------------------------ PRIVATE --------------------------
    
    private List<EnrichmentTag> findTagsWithReferences(List<Long> judgmentIds) {
        String select = buildSelectQuery();
        
        Query selectQuery = entityManager.createNativeQuery(select, EnrichmentTag.class);
        selectQuery.setParameter(JUDGMENT_IDS_QUERY_PARAM_NAME, judgmentIds.stream().map(id -> id.toString()).collect(Collectors.toList()));

        @SuppressWarnings("unchecked")
        List<EnrichmentTag> results = selectQuery.getResultList();
        
        return results;
    }
    
    private void removeReferencesFromTags(List<EnrichmentTag> tags, List<Long> judgmentIds) {
        tags.parallelStream().forEach(tag -> {
            try {
                TAG_TYPE tagValue = jsonStringParser.parseAndValidate(tag.getValue());

                removeReference(tagValue, judgmentIds);

                String value = jsonStringWriter.write(tagValue);
                tag.setValue(value);
                
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    
    //------------------------ SETTERS --------------------------

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setEnrichmentTagRepository(EnrichmentTagRepository enrichmentTagRepository) {
        this.enrichmentTagRepository = enrichmentTagRepository;
    }

    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    public void setJsonStringParser(JsonStringParser<TAG_TYPE> jsonStringParser) {
        this.jsonStringParser = jsonStringParser;
    }

    public void setJsonStringWriter(JsonStringWriter<TAG_TYPE> jsonStringWriter) {
        this.jsonStringWriter = jsonStringWriter;
    }

}
