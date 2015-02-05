package pl.edu.icm.saos.persistence.enrichment.model;

/**
 * String enrichment tag types that are recognizable and processed by SAOS.  <br/>
 * 
 * @author Łukasz Dumiszewski
 */

public final class EnrichmentTagTypes {

    
    private EnrichmentTagTypes() {
        throw new IllegalStateException("may not be instantiated");
    }
    
    
    
    /** ids of judgments whose text contents are 'similar' to a given judgment, similarity is specified by
     * algorithms in SAOS Enricher */
    public static final String SIMILAR_JUDGMENTS = "SIMILAR_JUDGMENTS";
    
    
    /** court cases referenced in a text content of a judgment */
    public static final String REFERENCED_COURT_CASES = "REFERENCED_COURT_CASES";
    
    
    /** referenced regulations referenced in a text content of a judgment and not provided by a judgment source system */
    public static final String REFERENCED_REGULATIONS = "REFERENCED_REGULATIONS";
}
