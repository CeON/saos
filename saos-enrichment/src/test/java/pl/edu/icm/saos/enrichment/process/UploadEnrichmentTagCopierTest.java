package pl.edu.icm.saos.enrichment.process;

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
    private UploadEnrichmentTagCopier uploadEnrichmentTagCopier;
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    @Autowired
    private UploadEnrichmentTagRepository uploadEnrichmentTagRepository;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    
    @Test
    public void copyUploadedEnrichmentTags() {
        
        // given
        
        Judgment ctJudgment = testPersistenceObjectFactory.createCtJudgment();
        
        UploadEnrichmentTag uploadEnrichmentTag1 = TestInMemoryUploadEnrichmentTagFactory.createUploadEnrichmentTag(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS, "{\"caseNumber\":\"sss\"}", ctJudgment.getId(), new DateTime(2012, 12, 12, 11, 13));
        UploadEnrichmentTag uploadEnrichmentTag2 = TestInMemoryUploadEnrichmentTagFactory.createUploadEnrichmentTag(EnrichmentTagTypes.REFERENCED_REGULATIONS, "{\"refReg\":\"11212ss\"}", ctJudgment.getId(), new DateTime(2012, 12, 12, 12, 11));
        UploadEnrichmentTag uploadEnrichmentTag3 = TestInMemoryUploadEnrichmentTagFactory.createUploadEnrichmentTag(EnrichmentTagTypes.REFERENCED_REGULATIONS, "{\"refReg\":\"1121ss\"}", ctJudgment.getId()+1000, new DateTime(2012, 12, 11, 12, 11));
        
        uploadEnrichmentTagRepository.save(Lists.newArrayList(uploadEnrichmentTag1, uploadEnrichmentTag2, uploadEnrichmentTag3));
        
        // execute
        
    }
    
    
}
