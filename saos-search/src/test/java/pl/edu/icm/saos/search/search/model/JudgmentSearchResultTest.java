package pl.edu.icm.saos.search.search.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author madryk
 */
public class JudgmentSearchResultTest {

    @Test
    public void getTotalPageNumber() {
        
        // execute & assert
        assertEquals(0, JudgmentSearchResult.getTotalPageNumber(0, 20));
        assertEquals(1, JudgmentSearchResult.getTotalPageNumber(1, 20));
        assertEquals(3, JudgmentSearchResult.getTotalPageNumber(30, 10));
        assertEquals(4, JudgmentSearchResult.getTotalPageNumber(31, 10));
    }
}
