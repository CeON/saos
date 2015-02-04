package pl.edu.icm.saos.enrichment.apply;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * Specifies a contract for classes converting TAG_VALUE to MODEL_OBJECT 
 * 
 * @param TAG_VALUE class representing the json value ({@link EnrichmentTag#getValue()}) of an {@link EnrichmentTag}.
 * In other words - the class to which the json value will be parsed. 
 * @param MODEL_OBJECT type of the object that will be constructed from TAG_VALUE and added to {@link Judgment} object tree
 * 

 * @author Łukasz Dumiszewski
 */

public interface EnrichmentTagValueConverter<TAG_VALUE, MODEL_OBJECT> {

    
    /**
     * Converts the given {@link #TAG_VALUE} to {@link #MODEL_OBJECT}
     */
    public MODEL_OBJECT convert(TAG_VALUE tagValue);
    
}
