package pl.edu.icm.saos.search.search.service;

import static junit.framework.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.CourtType;

/**
 * @author madryk
 */
public class SolrCriterionTransformerTest {

    private SolrCriterionTransformer<JudgmentIndexField> criterionTransformer = new SolrCriterionTransformer<JudgmentIndexField>();
    
    
    @Test
    public void transformCriterion_STRING() {
        String actual = criterionTransformer.transformCriterion(JudgmentIndexField.KEYWORD, "word");
        
        assertEquals("+keyword:word", actual);
    }
    
    @Test
    public void transformCriterion_INT() {
        String actual = criterionTransformer.transformCriterion(JudgmentIndexField.CC_COURT_ID, 12);
        
        assertEquals("+ccCourtId:12", actual);
    }
    
    @Test
    public void transformCriterion_ENUM() {
        String actual = criterionTransformer.transformCriterion(JudgmentIndexField.COURT_TYPE, CourtType.SUPREME);
        
        assertEquals("+courtType:SUPREME", actual);
    }
    
    @Test
    public void transformDateRangeCriterion() {
        String actual = criterionTransformer.transformDateRangeCriterion(
                JudgmentIndexField.JUDGMENT_DATE, new LocalDate(2012, 9, 23), new LocalDate(2013, 2, 6));
        
        assertEquals("+judgmentDate:[2012-09-23T00:00:00Z TO 2013-02-06T23:59:59Z]", actual);
    }
    
    @Test
    public void transformRangeCriterion() {
        String actual = criterionTransformer.transformRangeCriterion(JudgmentIndexField.KEYWORD, "aa", "ba");
        
        assertEquals("+keyword:[aa TO ba]", actual);
    }
    
}
