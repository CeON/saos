package pl.edu.icm.saos.persistence.enrichment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;


/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class EnrichmentTagRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
   
    private EnrichmentTag enrichmentTag = new EnrichmentTag();
    
     
    
    @Before
    public void before() {
        
        enrichmentTag.setTagType(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS);
        enrichmentTag.setValue("{caseNumbers: {caseNumber: 123, caseNumber: 234}}");
        
    }
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void saveAndFind() {
        
        // execute
        
        enrichmentTagRepository.save(enrichmentTag);
        
        EnrichmentTag dbEnrichmentTag = enrichmentTagRepository.findOne(enrichmentTag.getId());
        
        
        // assert
                
        assertTrue(enrichmentTag != dbEnrichmentTag);
        assertEquals(enrichmentTag, dbEnrichmentTag);
        

    }
   
}
