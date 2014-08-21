package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import pl.edu.icm.saos.common.testcommon.ReflectionFieldSetter;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class RawSourceCcJudgmentRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository;
    
    
    @Test
    public void findOneBySourceIdAndDataMd5_NotFound() {
        String sourceId = "12123+dsd";
        String dataMd5 = "sdwedj2irewnj,12nw";
        RawSourceCcJudgment rawSourceCcJudgment = rawSourceCcJudgmentRepository.findOneBySourceIdAndDataMd5(sourceId, dataMd5);
        
        assertNull(rawSourceCcJudgment);
        
    }
    
    
    @Test
    public void findOneBySourceIdAndDataMd5_Found() {
        String sourceId = "12123+dsd";
        String dataMd5 = "sdwedj2irewnj,12nw";
        
        RawSourceCcJudgment rawSourceCcJudgment = new RawSourceCcJudgment();
        rawSourceCcJudgment.setSourceId(sourceId);
        rawSourceCcJudgment.setDataMd5(dataMd5);
        rawSourceCcJudgment.setPublicationDate(new DateTime(2014, 06, 20, 23, 13, 2, 2));
        rawSourceCcJudgmentRepository.save(rawSourceCcJudgment);
        
        RawSourceCcJudgment dbRawSourceCcJudgment = rawSourceCcJudgmentRepository.findOneBySourceIdAndDataMd5(sourceId, dataMd5);
        
        assertNotNull(dbRawSourceCcJudgment);
        assertEquals(dbRawSourceCcJudgment.getSourceId(), rawSourceCcJudgment.getSourceId());
        assertEquals(dbRawSourceCcJudgment.getDataMd5(), rawSourceCcJudgment.getDataMd5());
    }
    
    
    @Test
    public void findMaxPublicationDate() {
        DateTime maxPublicationDate = rawSourceCcJudgmentRepository.findMaxPublicationDate();
        assertNull(maxPublicationDate);
        
        DateTime maxDateTime = new DateTime(2014, 06, 20, 23, 13, 2, 3);
        createAndSaveRawSourceCcJudgment(maxDateTime);
        createAndSaveRawSourceCcJudgment(new DateTime(2014, 06, 20, 23, 13, 2, 2));
        createAndSaveRawSourceCcJudgment(new DateTime(2011, 06, 20, 23, 13, 3));
        createAndSaveRawSourceCcJudgment(new DateTime(2013, 12, 20, 22, 13, 1));
        
        maxPublicationDate = rawSourceCcJudgmentRepository.findMaxPublicationDate();
        assertEquals(maxDateTime, maxPublicationDate);
        
    }

    
    @Test
    public void findNotProcessed_PAGE_FOUND() {
        
        RawSourceCcJudgment rJudgment0 = createAndSaveRawSourceCcJudgment(new DateTime(2013, 11, 20, 22, 13, 1), false);
        rJudgment0.setJustReasons(true);
        rawSourceCcJudgmentRepository.save(rJudgment0);
        
        RawSourceCcJudgment rJudgment1 = createAndSaveRawSourceCcJudgment(new DateTime(2014, 06, 20, 23, 13, 2, 2), false);
        RawSourceCcJudgment rJudgment2 = createAndSaveRawSourceCcJudgment(new DateTime(2011, 06, 20, 23, 13, 3), true);
        RawSourceCcJudgment rJudgment3 = createAndSaveRawSourceCcJudgment(new DateTime(2013, 12, 20, 22, 13, 1), false);
        
        
        Page<RawSourceCcJudgment> rJudgmentPage = rawSourceCcJudgmentRepository.findNotProcessed(new PageRequest(0, 10));
        assertEquals(3, rJudgmentPage.getTotalElements());
        assertEquals(3, rJudgmentPage.getContent().size());
        assertFalse(rJudgmentPage.getContent().contains(rJudgment2));
        
        assertEquals(rJudgment3, rJudgmentPage.getContent().get(0));
        assertEquals(rJudgment1, rJudgmentPage.getContent().get(1));
        assertEquals(rJudgment0, rJudgmentPage.getContent().get(2));
        
        
    }
    
    
    @Test
    public void findNotProcessed_PAGE_NOT_FOUND() {
        
        RawSourceCcJudgment rJudgment0 = createAndSaveRawSourceCcJudgment(new DateTime(2013, 11, 20, 22, 13, 1), false);
        rJudgment0.setJustReasons(true);
        rawSourceCcJudgmentRepository.save(rJudgment0);
        
        createAndSaveRawSourceCcJudgment(new DateTime(2014, 06, 20, 23, 13, 2, 2), false);
        createAndSaveRawSourceCcJudgment(new DateTime(2011, 06, 20, 23, 13, 3), true);
        createAndSaveRawSourceCcJudgment(new DateTime(2013, 12, 20, 22, 13, 1), false);
        
        Page<RawSourceCcJudgment> rJudgmentPage = rawSourceCcJudgmentRepository.findNotProcessed(new PageRequest(1, 10));
        assertEquals(3, rJudgmentPage.getTotalElements());
        assertEquals(0, rJudgmentPage.getContent().size());
        
    }
    
    
    @Test
    public void findOneBySourceIdAndProcessed() {
        RawSourceCcJudgment rJudgment0 = createAndSaveRawSourceCcJudgment(new DateTime(2013, 11, 20, 22, 13, 1), false);
        rJudgment0.setSourceId("1234A");
        rawSourceCcJudgmentRepository.save(rJudgment0);

        RawSourceCcJudgment rJudgment1 = createAndSaveRawSourceCcJudgment(new DateTime(2013, 11, 20, 22, 13, 1), true);
        rJudgment1.setSourceId("1234B");
        rawSourceCcJudgmentRepository.save(rJudgment1);

        RawSourceCcJudgment rJudgment2 = createAndSaveRawSourceCcJudgment(new DateTime(2013, 11, 20, 22, 13, 1), true);
        rJudgment2.setSourceId("1234B");
        rawSourceCcJudgmentRepository.save(rJudgment2);
        
        List<RawSourceCcJudgment> rJudgments = rawSourceCcJudgmentRepository.findBySourceIdAndProcessed(rJudgment0.getSourceId(), false);
        assertEquals(1, rJudgments.size());
        assertEquals(rJudgment0.getId(), rJudgments.get(0).getId());

        rJudgments = rawSourceCcJudgmentRepository.findBySourceIdAndProcessed(rJudgment1.getSourceId(), false);
        assertEquals(0, rJudgments.size());
        
    }
    
    //------------------------ PRIVATE --------------------------

    private RawSourceCcJudgment createAndSaveRawSourceCcJudgment(DateTime maxDateTime) {
        return createAndSaveRawSourceCcJudgment(maxDateTime, true);
    }
    
    private RawSourceCcJudgment createAndSaveRawSourceCcJudgment(DateTime maxDateTime, boolean processed) {
        RawSourceCcJudgment rawSourceCcJudgment = new RawSourceCcJudgment();
        rawSourceCcJudgment.setPublicationDate(maxDateTime);
        if (processed) {
            ReflectionFieldSetter.setField(rawSourceCcJudgment, "processed", processed);
        }
        rawSourceCcJudgment.setSourceId(UUID.randomUUID().toString());
        rawSourceCcJudgment.setDataMd5(UUID.randomUUID().toString());
        rawSourceCcJudgmentRepository.save(rawSourceCcJudgment);
        return rawSourceCcJudgment;
    }
}
