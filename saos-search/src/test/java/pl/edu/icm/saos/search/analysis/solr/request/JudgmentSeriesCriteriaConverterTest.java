package pl.edu.icm.saos.search.analysis.solr.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void convert_ccIncludeDependentCourtJudgments_FALSE() {
        
        // given
        JudgmentSeriesCriteria judgmentSeriesCriteria = new JudgmentSeriesCriteria();
        judgmentSeriesCriteria.setPhrase("XYZ");
        judgmentSeriesCriteria.setStartJudgmentDate(new LocalDate(2011, 11, 11));
        judgmentSeriesCriteria.setEndJudgmentDate(new LocalDate(2013, 12, 27));
        
        // execute
        JudgmentCriteria judgmentCriteria = judgmentSeriesCriteriaConverter.convert(judgmentSeriesCriteria);
        judgmentSeriesCriteria.setCcIncludeDependentCourtJudgments(false);
        
        // assert
        assertEquals(judgmentSeriesCriteria.getCcCourtId(), judgmentCriteria.getCcCourtId());
        assertNull(judgmentCriteria.getCcDirectOrSuperiorCourtId());
        assertCriteria(judgmentSeriesCriteria, judgmentCriteria);
        
    }

    
    @Test
    public void convert_ccIncludeDependentCourtJudgments_TRUE() {
        
        // given
        JudgmentSeriesCriteria judgmentSeriesCriteria = new JudgmentSeriesCriteria();
        judgmentSeriesCriteria.setPhrase("XYZ");
        judgmentSeriesCriteria.setStartJudgmentDate(new LocalDate(2011, 11, 11));
        judgmentSeriesCriteria.setEndJudgmentDate(new LocalDate(2013, 12, 27));
        judgmentSeriesCriteria.setCcIncludeDependentCourtJudgments(true);
        
        // execute
        JudgmentCriteria judgmentCriteria = judgmentSeriesCriteriaConverter.convert(judgmentSeriesCriteria);
        
        // assert
        assertEquals(judgmentSeriesCriteria.getCcCourtId(), judgmentCriteria.getCcDirectOrSuperiorCourtId());
        assertNull(judgmentCriteria.getCcCourtId());
        assertCriteria(judgmentSeriesCriteria, judgmentCriteria);
        
    }


    private void assertCriteria(JudgmentSeriesCriteria judgmentSeriesCriteria, JudgmentCriteria judgmentCriteria) {
        
        assertEquals(judgmentSeriesCriteria.getPhrase(), judgmentCriteria.getAll());
        assertEquals(judgmentSeriesCriteria.getStartJudgmentDate(), judgmentCriteria.getJudgmentDateFrom());
        assertEquals(judgmentSeriesCriteria.getEndJudgmentDate(), judgmentCriteria.getJudgmentDateTo());
        assertEquals(judgmentSeriesCriteria.getCourtType(), judgmentCriteria.getCourtType());
        assertEquals(judgmentSeriesCriteria.getCcCourtDivisionId(), judgmentCriteria.getCcCourtDivisionId());
        assertEquals(judgmentSeriesCriteria.getScCourtChamberId(), judgmentCriteria.getScCourtChamberId());
        assertEquals(judgmentSeriesCriteria.getScCourtChamberDivisionId(), judgmentCriteria.getScCourtChamberDivisionId());
    }

}
