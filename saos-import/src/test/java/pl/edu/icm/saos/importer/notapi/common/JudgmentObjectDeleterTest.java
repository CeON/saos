package pl.edu.icm.saos.importer.notapi.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.importer.ImportTestSupport;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class JudgmentObjectDeleterTest extends ImportTestSupport {

    @Autowired
    private JudgmentObjectDeleter judgmentObjectDeleter;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private RawJudgmentTestFactory rawJudgmentTestFactory;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void deleteJudgmentsWithoutRawSourceJudgment() {
        // given
        SupremeCourtJudgment scJudgment1 = rawJudgmentTestFactory.createScJudgment(true);
        rawJudgmentTestFactory.createScJudgment(false);
        ConstitutionalTribunalJudgment ctJudgment1 = rawJudgmentTestFactory.createCtJudgment(true);
        ConstitutionalTribunalJudgment ctJudgment2 = rawJudgmentTestFactory.createCtJudgment(false);
        
        // execute
        judgmentObjectDeleter.deleteJudgmentsWithoutRawSourceJudgment(SupremeCourtJudgment.class, RawSourceScJudgment.class);
        
        // assert
        List<Judgment> dbJudgments = judgmentRepository.findAll();
        
        assertThat(dbJudgments.stream().map(x -> x.getId()).collect(Collectors.toList()),
                containsInAnyOrder(scJudgment1.getId(), ctJudgment1.getId(), ctJudgment2.getId()));
    }
    

}
