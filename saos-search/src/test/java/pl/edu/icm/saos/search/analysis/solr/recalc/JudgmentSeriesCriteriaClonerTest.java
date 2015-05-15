package pl.edu.icm.saos.search.analysis.solr.recalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentSeriesCriteriaClonerTest {

    
    private JudgmentSeriesCriteriaCloner judgmentSeriesCriteriaCloner = new JudgmentSeriesCriteriaCloner();
    
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected=NullPointerException.class)
    public void clone_NULL() {
        
        // execute
        judgmentSeriesCriteriaCloner.clone(null);
        
    }
    

    @Test
    public void clone_NOT_NULL() {
    
        // given
        JudgmentSeriesCriteria sourceCriteria = new JudgmentSeriesCriteria();
        sourceCriteria.setCcCourtDivisionId(12L);
        sourceCriteria.setCcCourtId(10L);
        sourceCriteria.setCcIncludeDependentCourtJudgments(true);
        sourceCriteria.setCourtType(CourtType.COMMON);
        sourceCriteria.setScCourtChamberDivisionId(122L);
        sourceCriteria.setScCourtChamberId(1200L);
        sourceCriteria.setStartJudgmentDate(new LocalDate());
        sourceCriteria.setEndJudgmentDate(new LocalDate());
        sourceCriteria.setPhrase("CARS");
        
        
        // execute
        JudgmentSeriesCriteria clonedCriteria = judgmentSeriesCriteriaCloner.clone(sourceCriteria);
        
        // assert
        assertNotNull(clonedCriteria);
        assertFalse(sourceCriteria == clonedCriteria);
        assertEquals(sourceCriteria, clonedCriteria);
        
    }
    
    
    
}
