package pl.edu.icm.saos.persistence.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class RawSourceJudgmentRepositoryTest extends PersistenceTestSupport {

    @Autowired
    private RawSourceJudgmentRepository rawSourceJudgmentRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void findOne_FOUND() {
        // given
        RawSourceScJudgment rJudgment = createRawScJudgment("id1", false);
        createRawCtJudgment("id2", false);
        createRawCcJudgment("id3", true);
        
        // execute
        RawSourceScJudgment actualRawJudgment = rawSourceJudgmentRepository.findOne(rJudgment.getId(), RawSourceScJudgment.class);
        
        // assert
        assertThat(actualRawJudgment, is(notNullValue()));
        assertThat(actualRawJudgment.getId(), is(rJudgment.getId()));
    }
    
    @Test
    public void findOne_NOT_FOUND() {
        // given
        RawSourceScJudgment rJudgment = createRawScJudgment("id1", false);
        createRawCtJudgment("id2", false);
        createRawCcJudgment("id3", true);
        
        // execute
        RawSourceCtJudgment actual = rawSourceJudgmentRepository.findOne(rJudgment.getId(), RawSourceCtJudgment.class);
        
        // assert
        assertThat(actual, is(nullValue()));
    }
    
    @Test
    public void findOneBySourceId_FOUND() {
        // given
        RawSourceScJudgment rJudgment = createRawScJudgment("id1", false);
        createRawCtJudgment("id2", false);
        createRawCcJudgment("id3", false);
        
        // execute
        RawSourceScJudgment actualRawJudgment = rawSourceJudgmentRepository.findOneBySourceId("id1", RawSourceScJudgment.class);
        
        // assert
        assertThat(actualRawJudgment.getId(), is(rJudgment.getId()));
    }
    
    @Test
    public void findOneBySourceId_NOT_FOUND() {
        // given
        createRawScJudgment("id1", false);
        createRawCtJudgment("id2", false);
        createRawCcJudgment("id3", false);
        
        // execute
        RawSourceScJudgment actualRawJudgment = rawSourceJudgmentRepository.findOneBySourceId("id2", RawSourceScJudgment.class);
        
        // assert
        assertThat(actualRawJudgment, is(nullValue()));
    }
    
    @Test
    public void count() {
        // given
        createRawScJudgment("id1", false);
        createRawScJudgment("id2", false);
        createRawCtJudgment("id3", false);
        createRawCcJudgment("id4", false);
        createRawCcJudgment("id5", false);
        
        // execute
        long actualCount = rawSourceJudgmentRepository.count(RawSourceCcJudgment.class);
        
        // assert
        assertThat(actualCount, is(2L));
    }
    
    @Test
    public void findAllNotProcessedIds_FOUND() {
        // given
        RawSourceScJudgment rJudgment0 = createRawScJudgment("id1", false);
        RawSourceScJudgment rJudgment1 = createRawScJudgment("id2", false);
        createRawScJudgment("id3", true);
        createRawCtJudgment("id4", false);
        createRawCcJudgment("id5", true);
        
        // execute
        List<Integer> rJudgmentIds = rawSourceJudgmentRepository.findAllNotProcessedIds(RawSourceScJudgment.class);
        
        // assert
        assertThat(rJudgmentIds, containsInAnyOrder(rJudgment0.getId(), rJudgment1.getId()));
    }
    
    
    
    
    @Test
    public void findAllNotProcessedIds_NOT_FOUND() {
        // given
        createRawScJudgment("id1", true);
        createRawCtJudgment("id2", true);
        createRawCtJudgment("id3", false);
        createRawCcJudgment("id4", true);
        createRawCcJudgment("id5", false);
        
        // execute
        List<Integer> rJudgmentIds = rawSourceJudgmentRepository.findAllNotProcessedIds(RawSourceScJudgment.class);
        
        // assert
        assertThat(rJudgmentIds, is(empty()));
    }
    
    @Test
    public void deleteAll() {
        // given 
        createRawScJudgment("id1", false);
        createRawScJudgment("id2", false);
        RawSourceCtJudgment rCtJudgment = createRawCtJudgment("id3", false);
        RawSourceCcJudgment rCcJudgment = createRawCcJudgment("id4", false);
        
        
        // execute
        rawSourceJudgmentRepository.deleteAll(RawSourceScJudgment.class);
        
        
        // assert
        List<RawSourceJudgment> rawJudgments = rawSourceJudgmentRepository.findAll();
        List<Integer> rawJudgmentsIds = rawJudgments.stream()
                .map(x -> x.getId())
                .collect(Collectors.toList());
        
        assertThat(rawJudgmentsIds, containsInAnyOrder(rCtJudgment.getId(), rCcJudgment.getId()));
    }
    
    @Test(expected = DataIntegrityViolationException.class)
    public void save_INVALID_JSON_CONTENT() {
        // given
        RawSourceScJudgment rScJudgment = new RawSourceScJudgment();
        rScJudgment.setSourceId("id1");
        rScJudgment.setJsonContent("{\"aaa1\": \"bbb");
        
        // execute
        rawSourceJudgmentRepository.save(rScJudgment);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private RawSourceScJudgment createRawScJudgment(String sourceId, boolean processed) {
        RawSourceScJudgment rScJudgment = new RawSourceScJudgment();
        rScJudgment.setSourceId(sourceId);
        rScJudgment.setJsonContent("{\"aaa1\": \"bbb\"}");
        saveRawSourceJudgment(rScJudgment, processed);
        
        return rScJudgment;
    }
    
    private RawSourceCtJudgment createRawCtJudgment(String sourceId, boolean processed) {
        RawSourceCtJudgment rCtJudgment = new RawSourceCtJudgment();
        rCtJudgment.setSourceId(sourceId);
        rCtJudgment.setJsonContent("{\"aaa1\": \"bbb\"}");
        saveRawSourceJudgment(rCtJudgment, processed);
        
        return rCtJudgment;
    }
    
    private RawSourceCcJudgment createRawCcJudgment(String sourceId, boolean processed) {
        RawSourceCcJudgment rCcJudgment = new RawSourceCcJudgment();
        rCcJudgment.setSourceId(sourceId);
        saveRawSourceJudgment(rCcJudgment, processed);
        
        return rCcJudgment;
    }
    
    private void saveRawSourceJudgment(RawSourceJudgment rJudgment, boolean processed) {
        Whitebox.setInternalState(rJudgment, "processed", processed);
        rawSourceJudgmentRepository.save(rJudgment);
    }
    
}
