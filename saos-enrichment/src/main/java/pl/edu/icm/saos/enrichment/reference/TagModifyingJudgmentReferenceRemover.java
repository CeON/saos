package pl.edu.icm.saos.enrichment.reference;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.json.JsonFormatter;
import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * Abstract class for services that remove references to judgments from {@link EnrichmentTag}s.
 * Removing of judgment references involves modifying of tag value, see: {@link EnrichmentTag#getValue()}
 * 
 * @author madryk
 * @param <TAG_TYPE> - type of enrichment tag value type
 */
public abstract class TagModifyingJudgmentReferenceRemover<TAG_TYPE> implements TagJudgmentReferenceRemover {
    
    protected final static String JUDGMENT_IDS_QUERY_PARAM_NAME = "judgmentIds";
    
    
    private EntityManager entityManager;
    
    private EnrichmentTagRepository enrichmentTagRepository;
    
    private JudgmentRepository judgmentRepository;
    
    private JsonStringParser<TAG_TYPE> jsonStringParser;
    
    private JsonFormatter jsonFormatter;

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void removeReferences(List<Long> judgmentIds) {
        
        List<EnrichmentTag> enrichmentTags = findTagsWithReferences(judgmentIds);
        
        if (enrichmentTags.isEmpty()) {
            return;
        }

        removeReferencesFromTags(enrichmentTags, judgmentIds);
        
        enrichmentTagRepository.save(enrichmentTags);
        
        List<Long> judgmentsToReindex = enrichmentTags.stream().map(t -> t.getJudgmentId()).collect(Collectors.toList());
        judgmentRepository.markAsNotIndexed(judgmentsToReindex);
        
        
    }
    
    /**
     * Builds query for selecting enrichment tags that refer to judgments that are subject to deletion
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

                String value = jsonFormatter.formatObject(tagValue);
                tag.setValue(value);
                
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    public void setEnrichmentTagRepository(EnrichmentTagRepository enrichmentTagRepository) {
        this.enrichmentTagRepository = enrichmentTagRepository;
    }

    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    @Autowired
    public void setJsonStringParser(JsonStringParser<TAG_TYPE> jsonStringParser) {
        this.jsonStringParser = jsonStringParser;
    }

    @Autowired
    public void setJsonFormatter(JsonFormatter jsonFormatter) {
        this.jsonFormatter = jsonFormatter;
    }

}
