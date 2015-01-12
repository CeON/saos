package pl.edu.icm.saos.persistence.enrichment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTemp;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;


/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class EnrichmentTagTempRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private EnrichmentTagTempRepository enrichmentTagTempRepository;
    
   
    private EnrichmentTagTemp enrichmentTagTemp = new EnrichmentTagTemp();
    
     
    
    @Before
    public void before() {
        
        enrichmentTagTemp.setTagType(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS);
        enrichmentTagTemp.setValue("{\"caseNumbers\": [\"123\", \"234\"]}");
        
    }
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void saveAndFind() {
        
        // execute
        
        enrichmentTagTempRepository.save(enrichmentTagTemp);
        
        EnrichmentTagTemp dbEnrichmentTagTemp = enrichmentTagTempRepository.findOne(enrichmentTagTemp.getId());
        
        
        // assert
                
        assertTrue(enrichmentTagTemp != dbEnrichmentTagTemp);
        assertEquals(enrichmentTagTemp, dbEnrichmentTagTemp);
        

    }
    
    
    @Test(expected=DataIntegrityViolationException.class)
    public void save_InvalidJsonValue() {
        
        // given
        enrichmentTagTemp.setValue("{key:\"fff\"");
        
        // execute
        
        enrichmentTagTempRepository.save(enrichmentTagTemp);
        
        
    }
   
}
