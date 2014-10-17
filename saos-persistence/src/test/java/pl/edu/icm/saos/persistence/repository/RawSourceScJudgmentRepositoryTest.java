package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class RawSourceScJudgmentRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private RawSourceScJudgmentRepository rawSourceScJudgmentRepository;
    
    
   
    
    
    @Test
    public void findAllNotProcessedIds_Found() {
        
        RawSourceScJudgment rJudgment0 = createAndSaveSimpleRawSourceScJudgment(false);
        RawSourceScJudgment rJudgment1 = createAndSaveSimpleRawSourceScJudgment(false);
        RawSourceScJudgment rJudgment2 = createAndSaveSimpleRawSourceScJudgment(true);
        
        List<Integer> rJudgmentIds = rawSourceScJudgmentRepository.findAllNotProcessedIds();
        
        assertEquals(2, rJudgmentIds.size());
        
        assertTrue(rJudgmentIds.contains(rJudgment0.getId()));
        assertTrue(rJudgmentIds.contains(rJudgment1.getId()));
        assertFalse(rJudgmentIds.contains(rJudgment2.getId()));
    }
    
    
    
    
    @Test
    public void findAllNotProcessedIds_NotFound() {
        
        createAndSaveSimpleRawSourceScJudgment(true);
        createAndSaveSimpleRawSourceScJudgment(true);
        createAndSaveSimpleRawSourceScJudgment(true);
        
        
        List<Integer> rJudgmentIds = rawSourceScJudgmentRepository.findAllNotProcessedIds();
        
        assertEquals(0, rJudgmentIds.size());
        
    }
    
    
    
    
    //------------------------ PRIVATE --------------------------

    
    private RawSourceScJudgment createAndSaveSimpleRawSourceScJudgment(boolean processed) {
        RawSourceScJudgment rJudgment = new RawSourceScJudgment();
        rJudgment.setJsonContent("lskdlskdlksd l kdlksdlskd");
        Whitebox.setInternalState(rJudgment, "processed", processed);
        rawSourceScJudgmentRepository.save(rJudgment);
        return rJudgment;
        
    }
}
