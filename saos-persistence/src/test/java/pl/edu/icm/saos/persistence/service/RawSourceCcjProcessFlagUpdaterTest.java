package pl.edu.icm.saos.persistence.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.ReflectionFieldSetter;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.importer.ImportProcessingSkipReason;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class RawSourceCcjProcessFlagUpdaterTest extends PersistenceTestSupport {

    @Autowired
    private RawSourceCcjProcessFlagUpdater ccjProcessFlagUpdater;
    
    @Autowired
    private RawSourceCcJudgmentRepository rJudgmentRepository; 
    
    
    @Test
    public void markProcessedAllEligible() {
        RawSourceCcJudgment rJudgment1 = createAndSaveRawSourceCcJudgment(null, true);
        RawSourceCcJudgment rJudgment2 = createAndSaveRawSourceCcJudgment(null, true);
        RawSourceCcJudgment rJudgment3 = createAndSaveRawSourceCcJudgment(ImportProcessingSkipReason.COURT_DIVISION_NOT_FOUND, false);
        RawSourceCcJudgment rJudgment4 = createAndSaveRawSourceCcJudgment(ImportProcessingSkipReason.COURT_NOT_FOUND, false);
        RawSourceCcJudgment rJudgment5 = createAndSaveRawSourceCcJudgment(null, false);
        
        ccjProcessFlagUpdater.markProcessedAllEligible();
        
        assertTrue(rJudgmentRepository.findOne(rJudgment1.getId()).isProcessed());
        assertTrue(rJudgmentRepository.findOne(rJudgment2.getId()).isProcessed());
        assertFalse(rJudgmentRepository.findOne(rJudgment3.getId()).isProcessed());
        assertFalse(rJudgmentRepository.findOne(rJudgment4.getId()).isProcessed());
        assertFalse(rJudgmentRepository.findOne(rJudgment5.getId()).isProcessed());
    }
    
    
    private RawSourceCcJudgment createAndSaveRawSourceCcJudgment(ImportProcessingSkipReason skipReason, boolean processed) {
        RawSourceCcJudgment rawSourceCcJudgment = new RawSourceCcJudgment();
        rawSourceCcJudgment.setPublicationDate(new DateTime());
        if (processed) {
            ReflectionFieldSetter.setField(rawSourceCcJudgment, "processed", processed);
        }
        else {
           rawSourceCcJudgment.markProcessingSkipped(skipReason);
        }
        rawSourceCcJudgment.setSourceId(UUID.randomUUID().toString());
        rawSourceCcJudgment.setDataMd5(UUID.randomUUID().toString());
        rJudgmentRepository.save(rawSourceCcJudgment);
        return rawSourceCcJudgment;
    }
    
}
