package pl.edu.icm.saos.enrichment.apply;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class EnrichmentTagApplierManagerTest {

    private EnrichmentTagApplierManager enrichmentTagApplierManager = new EnrichmentTagApplierManager();
    
    @Mock private EnrichmentTagApplier enrichmentTagApplier1;
    
    @Mock private EnrichmentTagApplier enrichmentTagApplier2;
    
    
    private static final String ENRICHMENT_TAG_TYPE_1 = "enrichmentTagType1";
    
    private static final String ENRICHMENT_TAG_TYPE_2 = "enrichmentTagType2";
    
    
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        when(enrichmentTagApplier1.handlesEnrichmentTagType(ENRICHMENT_TAG_TYPE_1)).thenReturn(true);
        when(enrichmentTagApplier2.handlesEnrichmentTagType(ENRICHMENT_TAG_TYPE_2)).thenReturn(true);
        
        enrichmentTagApplierManager.setEnrichmentTagAppliers(Lists.newArrayList(enrichmentTagApplier1, enrichmentTagApplier2));
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void getEnrichmentTagApplier_NoProperApplierFound() {
        
        // given
        String nonHandledEnrichmentTagType = ENRICHMENT_TAG_TYPE_1 + ENRICHMENT_TAG_TYPE_2 + "X";
        
        // execute & assert
        assertNull(enrichmentTagApplierManager.getEnrichmentTagApplier(nonHandledEnrichmentTagType));
        
    }
    
    
    @Test
    public void getEnrichmentTagApplier_ProperApplierFound() {
        
        // execute
        EnrichmentTagApplier foundApplier = enrichmentTagApplierManager.getEnrichmentTagApplier(ENRICHMENT_TAG_TYPE_2);
        
        // assert
        assertTrue(foundApplier == enrichmentTagApplier2);
    }
    
    
}
