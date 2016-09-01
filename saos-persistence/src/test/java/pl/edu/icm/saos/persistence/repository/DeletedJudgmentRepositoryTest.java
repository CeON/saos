package pl.edu.icm.saos.persistence.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestInMemoryDeletedJudgmentFactory;
import pl.edu.icm.saos.persistence.model.DeletedJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class DeletedJudgmentRepositoryTest extends PersistenceTestSupport {

    @Autowired
    private DeletedJudgmentRepository deletedJudgmentRepository;

    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test
    public void findAllJudgmentIds() {
        
        // given
        DeletedJudgment judgment1 = createDeletedJudgment(123);
        DeletedJudgment judgment2 = createDeletedJudgment(124);
        DeletedJudgment judgment3 = createDeletedJudgment(125);
        
        // execute
        List<Long> judgmentIds = deletedJudgmentRepository.findAllJudgmentIds();
        
        // assert
        assertThat(judgmentIds, containsInAnyOrder(judgment1.getJudgmentId(), judgment2.getJudgmentId(), judgment3.getJudgmentId()));
    }
   
    
    
    //------------------------ PRIVATE --------------------------
    
    private DeletedJudgment createDeletedJudgment(long judgmentId) {
        
        DeletedJudgment deletedJudgment = TestInMemoryDeletedJudgmentFactory.createDeletedJudgment(judgmentId);
        
        deletedJudgmentRepository.save(deletedJudgment);
        
        return deletedJudgment;
    }
    
    
}
