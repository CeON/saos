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
    public void getOneWithClass_FOUND() {
        // given
        RawSourceScJudgment rJudgment = createRawScJudgment("id1", "aaa1", false);
        createRawCtJudgment("id2", "aaa2", false);
        createRawCcJudgment("id3", true);
        
        // execute
        RawSourceScJudgment actualRawJudgment = rawSourceJudgmentRepository.getOneWithClass(rJudgment.getId(), RawSourceScJudgment.class);
        
        // assert
        assertThat(actualRawJudgment, is(notNullValue()));
        assertThat(actualRawJudgment.getId(), is(rJudgment.getId()));
    }
    
    @Test
    public void getOneWithClass_NOT_FOUND() {
        // given
        RawSourceScJudgment rJudgment = createRawScJudgment("id1", "aaa1", false);
        createRawCtJudgment("id2", "aaa2", false);
        createRawCcJudgment("id3", true);
        
        // execute
        RawSourceCtJudgment actual = rawSourceJudgmentRepository.getOneWithClass(rJudgment.getId(), RawSourceCtJudgment.class);
        
        // assert
        assertThat(actual, is(nullValue()));
    }
    
    @Test
    public void findAllNotProcessedIdsWithClass_FOUND() {
        // given
        RawSourceScJudgment rJudgment0 = createRawScJudgment("id1", "aaa1", false);
        RawSourceScJudgment rJudgment1 = createRawScJudgment("id2", "aaa2", false);
        createRawScJudgment("id3", "aaa3", true);
        createRawCtJudgment("id4", "aaa4", false);
        createRawCcJudgment("id5", true);
        
        // execute
        List<Integer> rJudgmentIds = rawSourceJudgmentRepository.findAllNotProcessedIdsWithClass(RawSourceScJudgment.class);
        
        // assert
        assertThat(rJudgmentIds, containsInAnyOrder(rJudgment0.getId(), rJudgment1.getId()));
    }
    
    
    
    
    @Test
    public void findAllNotProcessedIdsWithClass_NOT_FOUND() {
        // given
        createRawScJudgment("id1", "aaa1", true);
        createRawCtJudgment("id2", "aaa2", true);
        createRawCtJudgment("id3", "aaa3", false);
        createRawCcJudgment("id4", true);
        createRawCcJudgment("id5", false);
        
        // execute
        List<Integer> rJudgmentIds = rawSourceJudgmentRepository.findAllNotProcessedIdsWithClass(RawSourceScJudgment.class);
        
        // assert
        assertThat(rJudgmentIds, is(empty()));
    }
    
    @Test
    public void deleteAllWithClass() {
        // given 
        createRawScJudgment("id1", "aaa1", false);
        createRawScJudgment("id2", "aaa2", false);
        RawSourceCtJudgment rCtJudgment = createRawCtJudgment("id3", "aaa3", false);
        RawSourceCcJudgment rCcJudgment = createRawCcJudgment("id4", false);
        
        
        // execute
        rawSourceJudgmentRepository.deleteAllWithClass(RawSourceScJudgment.class);
        
        
        // assert
        List<RawSourceJudgment> rawJudgments = rawSourceJudgmentRepository.findAll();
        List<Integer> rawJudgmentsIds = rawJudgments.stream()
                .map(x -> x.getId())
                .collect(Collectors.toList());
        
        assertThat(rawJudgmentsIds, containsInAnyOrder(rCtJudgment.getId(), rCcJudgment.getId()));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private RawSourceScJudgment createRawScJudgment(String sourceId, String jsonContent, boolean processed) {
        RawSourceScJudgment rScJudgment = new RawSourceScJudgment();
        rScJudgment.setJsonContent(sourceId);
        rScJudgment.setSourceId(jsonContent);
        saveRawSourceJudgment(rScJudgment, processed);
        
        return rScJudgment;
    }
    
    private RawSourceCtJudgment createRawCtJudgment(String sourceId, String jsonContent, boolean processed) {
        RawSourceCtJudgment rCtJudgment = new RawSourceCtJudgment();
        rCtJudgment.setSourceId(sourceId);
        rCtJudgment.setJsonContent(jsonContent);
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
