package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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


    private void createAndSaveRawSourceCcJudgment(DateTime maxDateTime) {
        RawSourceCcJudgment rawSourceCcJudgment = new RawSourceCcJudgment();
        rawSourceCcJudgment.setPublicationDate(maxDateTime);
        rawSourceCcJudgmentRepository.save(rawSourceCcJudgment);
    }
    
}
