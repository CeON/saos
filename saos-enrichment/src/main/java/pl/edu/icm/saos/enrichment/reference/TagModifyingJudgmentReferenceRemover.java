package pl.edu.icm.saos.enrichment.reference;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.json.JsonStringWriter;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * Class for removing reference to judgment from {@link EnrichmentTag}.
 * It supports tags where reference removing is based on modifying {@link EnrichmentTag#getValue()}.
 * 
 * @author madryk
 * @param <TAG_TYPE> - type of enrichment tag value type
 */
public abstract class TagModifyingJudgmentReferenceRemover<TAG_TYPE> implements JudgmentReferenceRemover {
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    @Autowired
    private JsonStringParser<TAG_TYPE> jsonStringParser;
    
    @Autowired
    private JsonStringWriter<TAG_TYPE> jsonStringWriter;
    
    
    private String tagType;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public TagModifyingJudgmentReferenceRemover(String tagType) {
        this.tagType = tagType;
    }

    
    //------------------------ LOGIC --------------------------
    
    @Override
    @Transactional
    public void removeReference(long judgmentId) {
        
        String selectQuery = buildSelectQuery();
        
        Query query = entityManager.createNativeQuery(selectQuery, EnrichmentTag.class);
        query.setParameter("tagType", tagType);
        query.setParameter("judgmentId", String.valueOf(judgmentId));
        
        @SuppressWarnings("unchecked")
        List<EnrichmentTag> results = query.getResultList();
        
        
        for (EnrichmentTag result : results) {
            try {
                TAG_TYPE tagValue = jsonStringParser.parseAndValidate(result.getValue());

                removeReference(tagValue, judgmentId);

                String value = jsonStringWriter.write(tagValue);
                result.setValue(value);

                enrichmentTagRepository.save(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
    }
    
    /**
     * Builds query for selecting enrichment tags which reference to judgment need to be removed
     */
    protected abstract String buildSelectQuery();
    
    /**
     * Removes reference to judgment from enrichment tag value
     */
    protected abstract void removeReference(TAG_TYPE tagValue, long judgmentId);
    
    
    //------------------------ SETTERS --------------------------

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setEnrichmentTagRepository(EnrichmentTagRepository enrichmentTagRepository) {
        this.enrichmentTagRepository = enrichmentTagRepository;
    }

    public void setJsonStringParser(JsonStringParser<TAG_TYPE> jsonStringParser) {
        this.jsonStringParser = jsonStringParser;
    }

    public void setJsonStringWriter(JsonStringWriter<TAG_TYPE> jsonStringWriter) {
        this.jsonStringWriter = jsonStringWriter;
    }

}
