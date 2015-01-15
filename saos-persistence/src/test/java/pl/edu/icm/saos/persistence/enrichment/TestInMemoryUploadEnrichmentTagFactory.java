package pl.edu.icm.saos.persistence.enrichment;

import org.joda.time.DateTime;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;

/**
 * @author Łukasz Dumiszewski
 */

public final class TestInMemoryUploadEnrichmentTagFactory {
   
    
	
	
	//------------------------ CONSTRUCTORS --------------------------
	
    private TestInMemoryUploadEnrichmentTagFactory() {
        
        throw new IllegalStateException("may not be instantiated");
        
    }
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    public static UploadEnrichmentTag createUploadEnrichmentTag(String enrichmentTagType, String enrichmentTagValue) {
        UploadEnrichmentTag uploadEnrichmentTag = new UploadEnrichmentTag();
        uploadEnrichmentTag.setTagType(enrichmentTagType);
        uploadEnrichmentTag.setValue(enrichmentTagValue);
        return uploadEnrichmentTag;
    }
    
    public static UploadEnrichmentTag createUploadEnrichmentTag(String enrichmentTagType, String enrichmentTagValue, int judgmentId, DateTime creationDate) {
        UploadEnrichmentTag uploadEnrichmentTag = createUploadEnrichmentTag(enrichmentTagType, enrichmentTagValue);
        uploadEnrichmentTag.setJudgmentId(judgmentId);
        Whitebox.setInternalState(uploadEnrichmentTag, "creationDate", creationDate);
        return uploadEnrichmentTag;
    }
}
