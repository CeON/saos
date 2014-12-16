package pl.edu.icm.saos.persistence.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.experimental.categories.Category;
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
    public void deleteAllWithClass() {
        createRawScJudgment("id1", "aaa1");
        createRawScJudgment("id2", "aaa2");
        RawSourceCtJudgment rCtJudgment = createRawCtJudgment("id3", "aaa3");
        RawSourceCcJudgment rCcJudgment = createRawCcJudgment("aaa4");
        
        
        rawSourceJudgmentRepository.deleteAllWithClass(RawSourceScJudgment.class);
        
        
        List<RawSourceJudgment> rawJudgments = rawSourceJudgmentRepository.findAll();
        List<Integer> rawJudgmentsIds = rawJudgments.stream()
                .map(x -> x.getId())
                .collect(Collectors.toList());
        
        assertThat(rawJudgmentsIds, containsInAnyOrder(rCtJudgment.getId(), rCcJudgment.getId()));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private RawSourceScJudgment createRawScJudgment(String sourceId, String jsonContent) {
        RawSourceScJudgment rScJudgment = new RawSourceScJudgment();
        rScJudgment.setJsonContent(sourceId);
        rScJudgment.setSourceId(jsonContent);
        rawSourceJudgmentRepository.save(rScJudgment);
        
        return rScJudgment;
    }
    
    private RawSourceCtJudgment createRawCtJudgment(String sourceId, String jsonContent) {
        RawSourceCtJudgment rCtJudgment = new RawSourceCtJudgment();
        rCtJudgment.setSourceId(sourceId);
        rCtJudgment.setJsonContent(jsonContent);
        rawSourceJudgmentRepository.save(rCtJudgment);
        
        return rCtJudgment;
    }
    
    private RawSourceCcJudgment createRawCcJudgment(String sourceId) {
        RawSourceCcJudgment rCcJudgment = new RawSourceCcJudgment();
        rCcJudgment.setSourceId(sourceId);
        rawSourceJudgmentRepository.save(rCcJudgment);
        
        return rCcJudgment;
    }
}
