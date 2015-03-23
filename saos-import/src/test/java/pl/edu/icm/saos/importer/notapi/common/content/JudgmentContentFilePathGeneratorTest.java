package pl.edu.icm.saos.importer.notapi.common.content;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * @author madryk
 */
public class JudgmentContentFilePathGeneratorTest {

    private JudgmentContentFilePathGenerator judgmentContentFilePathGenerator = new JudgmentContentFilePathGenerator();
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void generatePath() {
        // given
        Judgment judgment = new SupremeCourtJudgment();
        judgment.setJudgmentDate(new LocalDate(2012, 2, 12));
        
        // execute
        String generatedPath = judgmentContentFilePathGenerator.generatePath(judgment);
        
        // assert
        assertEquals("supreme/2012/2/12/", generatedPath);
    }
}
