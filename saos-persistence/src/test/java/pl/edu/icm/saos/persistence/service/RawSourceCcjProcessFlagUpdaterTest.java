package pl.edu.icm.saos.persistence.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.importer.ImportProcessingStatus;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class RawSourceCcjProcessFlagUpdaterTest extends PersistenceTestSupport {

    @Autowired
    private RawSourceCcjProcessFlagUpdater ccjProcessFlagUpdater;
    
    @Autowired
    private RawSourceCcJudgmentRepository rJudgmentRepository; 
    
    
    @Test
    public void markProcessedAllEligible() {
        RawSourceCcJudgment rJudgment1 = createAndSaveRawSourceCcJudgment(ImportProcessingStatus.OK, false);
        RawSourceCcJudgment rJudgment2 = createAndSaveRawSourceCcJudgment(ImportProcessingStatus.OK, true);
        RawSourceCcJudgment rJudgment3 = createAndSaveRawSourceCcJudgment(ImportProcessingStatus.SKIPPED, false);
        RawSourceCcJudgment rJudgment4 = createAndSaveRawSourceCcJudgment(ImportProcessingStatus.SKIPPED, false);
        RawSourceCcJudgment rJudgment5 = createAndSaveRawSourceCcJudgment(null, false);
        
        ccjProcessFlagUpdater.markProcessedAllEligible();
        
        assertTrue(rJudgmentRepository.findOne(rJudgment1.getId()).isProcessed());
        assertTrue(rJudgmentRepository.findOne(rJudgment2.getId()).isProcessed());
        assertFalse(rJudgmentRepository.findOne(rJudgment3.getId()).isProcessed());
        assertFalse(rJudgmentRepository.findOne(rJudgment4.getId()).isProcessed());
        assertFalse(rJudgmentRepository.findOne(rJudgment5.getId()).isProcessed());
    }
    
    
    private RawSourceCcJudgment createAndSaveRawSourceCcJudgment(ImportProcessingStatus processingStatus, boolean processed) {
        RawSourceCcJudgment rawSourceCcJudgment = new RawSourceCcJudgment();
        rawSourceCcJudgment.setPublicationDate(new DateTime());
        if (processed) {
            rawSourceCcJudgment.markProcessedOk();
        }
        rawSourceCcJudgment.updateProcessingStatus(processingStatus);
        rawSourceCcJudgment.setSourceId(UUID.randomUUID().toString());
        rawSourceCcJudgment.setDataMd5(UUID.randomUUID().toString());
        rJudgmentRepository.save(rawSourceCcJudgment);
        return rawSourceCcJudgment;
    }
    
}
