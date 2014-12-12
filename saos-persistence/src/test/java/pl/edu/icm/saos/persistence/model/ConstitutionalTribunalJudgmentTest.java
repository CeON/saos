package pl.edu.icm.saos.persistence.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class ConstitutionalTribunalJudgmentTest {

    private ConstitutionalTribunalJudgment judgment = new ConstitutionalTribunalJudgment();
    
    private ConstitutionalTribunalJudgmentDissentingOpinion firstOpinion = new ConstitutionalTribunalJudgmentDissentingOpinion();
    
    private ConstitutionalTribunalJudgmentDissentingOpinion secondOpinion = new ConstitutionalTribunalJudgmentDissentingOpinion();
    
    
    @Before
    public void setUp() {
        firstOpinion.setTextContent("textContent");
        firstOpinion.addAuthor("Jan Kowalski");
        firstOpinion.addAuthor("Adam Nowak");
        
        secondOpinion.setTextContent("textContent2");
        secondOpinion.addAuthor("Piotr Nowakowski");
        
        judgment.addDissentingOpinion(firstOpinion);
        judgment.addDissentingOpinion(secondOpinion);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void getDissentingOpinions() {
        assertEquals(Lists.newArrayList(firstOpinion, secondOpinion), judgment.getDissentingOpinions());
    }
    
    @Test
    public void containsDissentingOpinion_FOUND() {
        assertTrue(judgment.containsDissentingOpinion(firstOpinion));
        assertTrue(judgment.containsDissentingOpinion(secondOpinion));
    }
    
    @Test
    public void containsDissentingOpinion_NOT_FOUND() {
        ConstitutionalTribunalJudgmentDissentingOpinion opinion = new ConstitutionalTribunalJudgmentDissentingOpinion();
        opinion.setTextContent("textContent");
        opinion.addAuthor("Jan Kowalski");
        
        assertFalse(judgment.containsDissentingOpinion(opinion));
    }
    
    @Test
    public void removeDissentingOpinion() {
        judgment.removeDissentingOpinion(firstOpinion);
        
        assertEquals(Lists.newArrayList(secondOpinion), judgment.getDissentingOpinions());
    }
    
}
