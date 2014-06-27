package pl.edu.icm.saos.importer.commoncourt.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.importer.commoncourt.process.CcJudgmentKeywordCreator;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.repository.CcJudgmentKeywordRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class CcJudgmentKeywordCreatorTest {

    
    private CcJudgmentKeywordCreator keywordCreator = new CcJudgmentKeywordCreator();
    
    
    private CcJudgmentKeywordRepository ccJudgmentKeywordRepository = mock(CcJudgmentKeywordRepository.class);
    
    
    private CcJudgmentKeyword keyword = new CcJudgmentKeyword();
    
    
    @Before
    public void before() {
        
        keyword.setPhrase("BLEBLE");
        
        when(ccJudgmentKeywordRepository.findOneByPhrase(Mockito.eq(keyword.getPhrase()))).thenReturn(keyword);
        
        keywordCreator.setCcJudgmentKeywordRepository(ccJudgmentKeywordRepository);
        
        
    }

    
    @Test
    public void testGetOrCreateCcJudgmentKeyword_Create() {
        
        String phrase = keyword.getPhrase() + "YYY";
        CcJudgmentKeyword foundKeyword = keywordCreator.getOrCreateCcJudgmentKeyword(phrase);
        
        verify(ccJudgmentKeywordRepository).save(Mockito.any(CcJudgmentKeyword.class));
        verify(ccJudgmentKeywordRepository).findOneByPhrase(phrase);
        
        assertEquals(phrase, foundKeyword.getPhrase());
        assertFalse(foundKeyword == keyword);
        
               
    }
    
    
    @Test
    public void testGetOrCreateCcJudgmentKeyword_Get() {
        
        String phrase = keyword.getPhrase();
        CcJudgmentKeyword foundKeyword = keywordCreator.getOrCreateCcJudgmentKeyword(phrase);
        
        verify(ccJudgmentKeywordRepository, never()).save(Mockito.any(CcJudgmentKeyword.class));
        verify(ccJudgmentKeywordRepository).findOneByPhrase(phrase);
        
        assertEquals(phrase, foundKeyword.getPhrase());
        assertTrue(foundKeyword == keyword);
        
               
    }
    

}
