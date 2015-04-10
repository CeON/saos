package pl.edu.icm.saos.search.analysis.solr.request;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
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
        judgmentSeriesCriteria.setStartJudgmentDate(new LocalDate(2011, 11, 11));
        judgmentSeriesCriteria.setEndJudgmentDate(new LocalDate(2013, 12, 27));
        
        // execute
        JudgmentCriteria judgmentCriteria = judgmentSeriesCriteriaConverter.convert(judgmentSeriesCriteria);
        
        // assert
        assertEquals(judgmentSeriesCriteria.getPhrase(), judgmentCriteria.getAll());
        assertEquals(judgmentSeriesCriteria.getStartJudgmentDate(), judgmentCriteria.getJudgmentDateFrom());
        assertEquals(judgmentSeriesCriteria.getEndJudgmentDate(), judgmentCriteria.getJudgmentDateTo());
    }
    
}
