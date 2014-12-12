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
public class ConstitutionalTribunalJudgmentDissentingOpinionTest {

    private ConstitutionalTribunalJudgmentDissentingOpinion dissentingOpinion = new ConstitutionalTribunalJudgmentDissentingOpinion();
    
    private static final String[] AUTHORS = { "Jan Kowalski", "Adam Nowak", "Piotr Nowakowski" };
    private static final String AUTHOR_NOT_EXIST = "Not Exists";
    
    @Before
    public void setUp() {
        dissentingOpinion.addAuthor(AUTHORS[0]);
        dissentingOpinion.addAuthor(AUTHORS[1]);
        dissentingOpinion.addAuthor(AUTHORS[2]);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void getAuthors() {
        assertEquals(Lists.newArrayList(AUTHORS[0], AUTHORS[1], AUTHORS[2]), dissentingOpinion.getAuthors());
    }
    
    @Test
    public void containsAuthor() {
        assertTrue(dissentingOpinion.containsAuthor(AUTHORS[0]));
        assertTrue(dissentingOpinion.containsAuthor(AUTHORS[1]));
        assertTrue(dissentingOpinion.containsAuthor(AUTHORS[2]));
        assertFalse(dissentingOpinion.containsAuthor(AUTHOR_NOT_EXIST));
    }
    
    @Test
    public void removeAuthor() {
        dissentingOpinion.removeAuthor(AUTHORS[1]);
        
        assertEquals(Lists.newArrayList(AUTHORS[0], AUTHORS[2]), dissentingOpinion.getAuthors());
    }
}
