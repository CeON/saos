package pl.edu.icm.saos.enrichment.apply;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.base.Preconditions;

/**
 * The default implementation of {@link EnrichmentTagApplier} facilitating creation of specific tag appliers for different
 * {@link EnrichmentTag}s.
 * 
 * @param TAG_VALUE class representing the json value ({@link EnrichmentTag#getValue()}) of an {@link EnrichmentTag}.
 * In other words - the class to which the json value will be parsed. 
 * @param MODEL_OBJECT type of the object that will be constructed from TAG_VALUE and added to {@link Judgment} object tree
 * 
 * @author Łukasz Dumiszewski
 */

public class DefaultEnrichmentTagApplier<TAG_VALUE, MODEL_OBJECT> implements EnrichmentTagApplier {
    
    
    
    private JsonStringParser<TAG_VALUE> jsonStringParser;
    
    private EnrichmentTagValueConverter<TAG_VALUE, MODEL_OBJECT> enrichmentTagValueConverter;
    
    private JudgmentUpdater<MODEL_OBJECT> judgmentUpdater;
    
    private String handledEnrichmentTagType;
    
    

    //------------------------ CONSTRUCTORS --------------------------
    
    public DefaultEnrichmentTagApplier(String handledEnrichmentTagType) {
        super();
        Preconditions.checkNotNull(handledEnrichmentTagType);
        this.handledEnrichmentTagType = handledEnrichmentTagType;
    }

    
    
    // ------------------------ LOGIC --------------------------
    
    @Override
    public void applyEnrichmentTag(Judgment judgment, EnrichmentTag enrichmentTag) {
        
        Preconditions.checkNotNull(judgment);
        Preconditions.checkNotNull(enrichmentTag);
        Preconditions.checkArgument(handlesEnrichmentTagType(enrichmentTag.getTagType()));
        
        
        TAG_VALUE enrichmentTagValue;
        
        try {
            
            enrichmentTagValue = jsonStringParser.parseAndValidate(enrichmentTag.getValue());
        
        } catch (JsonParseException e) {
            
            throw new IllegalArgumentException(e);
            
        }
        
        
        MODEL_OBJECT modelObject = enrichmentTagValueConverter.convert(enrichmentTagValue);
            
        judgmentUpdater.addToJudgment(judgment, modelObject);
        
        
    }

    @Override
    public boolean handlesEnrichmentTagType(String enrichmentTagType) {
        return handledEnrichmentTagType.equals(enrichmentTagType);
    }




    
    //------------------------ SETTERS --------------------------

    /**
     * The passed parser will be used to parse {@link EnrichmentTag#getValue()} to {@link #TAG_VALUE} class
     */
    public void setJsonStringParser(JsonStringParser<TAG_VALUE> jsonStringParser) {
        this.jsonStringParser = jsonStringParser;
    }


    /**
     * The converter will convert {@link #TAG_VALUE} created by the {@link #setJsonStringParser(JsonStringParser)} to {@link #MODEL_OBJECT} 
     */
    public void setEnrichmentTagValueConverter(EnrichmentTagValueConverter<TAG_VALUE, MODEL_OBJECT> enrichmentTagValueConverter) {
        this.enrichmentTagValueConverter = enrichmentTagValueConverter;
    }


    /**
     * This service will add the {@link #MODEL_OBJECT} created by {@link #setEnrichmentTagValueConverter(EnrichmentTagValueConverter)} to
     * {@link Judgment} 
     */
    public void setJudgmentUpdater(JudgmentUpdater<MODEL_OBJECT> judgmentUpdater) {
        this.judgmentUpdater = judgmentUpdater;
    }
}
