package pl.edu.icm.saos.search.indexing;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author madryk
 */
public class JudgmentResetIndexFlagProcessorTest {

    private JudgmentResetIndexFlagProcessor judgmentResetIndexFlagProcessor = new JudgmentResetIndexFlagProcessor();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void process() throws Exception {
        Judgment judgment = new CommonCourtJudgment();
        judgment.markAsIndexed();
        
        judgmentResetIndexFlagProcessor.process(judgment);
        
        assertFalse(judgment.isIndexed());
    }
}
