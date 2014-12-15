package pl.edu.icm.saos.enrichment.upload;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * @author Łukasz Dumiszewski
 */

public class EnrichmentTagItemConverterTest {

    
    private EnrichmentTagItemConverter converter = new EnrichmentTagItemConverter();
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected=NullPointerException.class)
    public void convert_null() {
        
        converter.convertEnrichmentTagItem(null);
        
    }
    
    
    
    @Test
    public void convert() {
        
        // given
        
        EnrichmentTagItem tagItem = new EnrichmentTagItem();
        tagItem.setJudgmentId(123);
        tagItem.setTagType("A TYPE");
        tagItem.setValue("A VALUE");
        
        
        // execute
        
        EnrichmentTag enrichmentTag = converter.convertEnrichmentTagItem(tagItem);
        
        
        // assert
        
        assertEquals(tagItem.getJudgmentId(), enrichmentTag.getJudgmentId());
        assertEquals(tagItem.getTagType(), enrichmentTag.getTagType());
        assertEquals(tagItem.getValue(), enrichmentTag.getValue());
    }
    
    
}
