package pl.edu.icm.saos.enrichment.process;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.enrichment.EnrichmentTestSupport;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.TestInMemoryUploadEnrichmentTagFactory;
import pl.edu.icm.saos.persistence.enrichment.UploadEnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class) 
public class UploadEnrichmentTagCopierTest extends EnrichmentTestSupport {

    @Autowired
    private UploadEnrichmentTagCopier uploadEnrichmentTagCopier = new UploadEnrichmentTagCopier();
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    @Autowired
    private UploadEnrichmentTagRepository uploadEnrichmentTagRepository;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    
    
    //------------------------ TESTS --------------------------
    
    
    
    @Test
    public void copyUploadedEnrichmentTags() {
        
        // given
        
        Judgment ctJudgment = testPersistenceObjectFactory.createCtJudgment();
        
        UploadEnrichmentTag uploadEnrichmentTag1 = TestInMemoryUploadEnrichmentTagFactory.createUploadEnrichmentTag(EnrichmentTagTypes.REFERENCED_COURT_CASES, "{\"caseNumber\":\"sss\"}", ctJudgment.getId(), new DateTime(2012, 12, 12, 11, 13));
        UploadEnrichmentTag uploadEnrichmentTag2 = TestInMemoryUploadEnrichmentTagFactory.createUploadEnrichmentTag(EnrichmentTagTypes.REFERENCED_REGULATIONS, "{\"refReg\":\"11212ss\"}", ctJudgment.getId(), new DateTime(2012, 12, 12, 12, 11));
        UploadEnrichmentTag uploadEnrichmentTag3 = TestInMemoryUploadEnrichmentTagFactory.createUploadEnrichmentTag(EnrichmentTagTypes.REFERENCED_REGULATIONS, "{\"refReg\":\"1121ss\"}", ctJudgment.getId()+1000, new DateTime(2012, 12, 11, 12, 11));
        
        uploadEnrichmentTagRepository.save(Lists.newArrayList(uploadEnrichmentTag1, uploadEnrichmentTag2, uploadEnrichmentTag3));
        
        assertEquals(0, enrichmentTagRepository.count());
        
        // execute
     
        uploadEnrichmentTagCopier.copyUploadedEnrichmentTags();
        
        
        // assert
        
        assertEquals(2, enrichmentTagRepository.count());
        assertThat(enrichmentTagRepository.findAll(), containsInAnyOrder(transform(uploadEnrichmentTag1), transform(uploadEnrichmentTag2)));
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private EnrichmentTag transform(UploadEnrichmentTag uploadEnrichmentTag) {
    	
    	EnrichmentTag enrichmentTag = new EnrichmentTag();
    	
    	enrichmentTag.setJudgmentId(uploadEnrichmentTag.getJudgmentId());
    	enrichmentTag.setTagType(uploadEnrichmentTag.getTagType());
    	enrichmentTag.setValue(uploadEnrichmentTag.getValue());
    	
    	return enrichmentTag;        
    }
    
    
}
