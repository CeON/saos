package pl.edu.icm.saos.search.analysis.solr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentSeriesCriteriaConverterTest {

    
    private JudgmentSeriesCriteriaConverter judgmentSeriesCriteriaConverter = new JudgmentSeriesCriteriaConverter();
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void convert() {
        
        // given
        JudgmentSeriesCriteria judgmentSeriesCriteria = new JudgmentSeriesCriteria();
        judgmentSeriesCriteria.setPhrase("XYZ");
        
        // execute
        JudgmentCriteria judgmentCriteria = judgmentSeriesCriteriaConverter.convert(judgmentSeriesCriteria);
        
        // assert
        assertEquals(judgmentSeriesCriteria.getPhrase(), judgmentCriteria.getAll());
        
    }
    
}
