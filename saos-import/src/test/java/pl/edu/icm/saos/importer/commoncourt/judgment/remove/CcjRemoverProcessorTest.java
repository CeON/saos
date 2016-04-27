package pl.edu.icm.saos.importer.commoncourt.judgment.remove;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.RemovedJudgment;
import pl.edu.icm.saos.persistence.model.SourceCode;

/**
 * @author madryk
 */
public class CcjRemoverProcessorTest {

    private CcjRemoverProcessor ccjRemoverProcessor = new CcjRemoverProcessor();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void process() throws Exception {
        
        // given
        
        Judgment judgment = new CommonCourtJudgment();
        
        Whitebox.setInternalState(judgment, "id", 3L);
        judgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        judgment.getSourceInfo().setSourceJudgmentId("sourceId");
        judgment.getSourceInfo().setSourceJudgmentUrl("sourceUrl");
        
        
        // execute
        
        RemovedJudgment judgmentToRemove = ccjRemoverProcessor.process(judgment);
        
        
        // assert
        
        assertEquals(3L, judgmentToRemove.getRemovedJudgmentId());
        assertEquals(SourceCode.COMMON_COURT, judgmentToRemove.getSourceInfo().getSourceCode());
        assertEquals("sourceId", judgmentToRemove.getSourceInfo().getSourceJudgmentId());
        assertEquals("sourceUrl", judgmentToRemove.getSourceInfo().getSourceJudgmentUrl());
        
    }
    
}
