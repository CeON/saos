package pl.edu.icm.saos.importer.common;

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

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.persistence.repository.JudgmentKeywordRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentKeywordCreatorTest {

    
    private JudgmentKeywordCreator keywordCreator = new JudgmentKeywordCreator();
    
    
    private JudgmentKeywordRepository judgmentKeywordRepository = mock(JudgmentKeywordRepository.class);
    
    
    private JudgmentKeyword keyword = new JudgmentKeyword(CourtType.COMMON, "BLEBLE");
    
    
    @Before
    public void before() {
        
        when(judgmentKeywordRepository.findOneByCourtTypeAndPhraseIgnoreCase(CourtType.COMMON, keyword.getPhrase())).thenReturn(keyword);
        
        keywordCreator.setJudgmentKeywordRepository(judgmentKeywordRepository);
        
        
    }

    
    @Test
    public void testGetOrCreateJudgmentKeyword_Create() {
        
        // given
        
        String phrase = keyword.getPhrase() + "YYY";
        CourtType courtType = CourtType.COMMON;
        
        // execute
        
        JudgmentKeyword foundKeyword = keywordCreator.getOrCreateJudgmentKeyword(courtType, phrase);
        
        
        // assert
        
        verify(judgmentKeywordRepository).save(Mockito.any(JudgmentKeyword.class));
        verify(judgmentKeywordRepository).findOneByCourtTypeAndPhraseIgnoreCase(courtType, phrase);
        
        assertFalse(foundKeyword == keyword);
        assertEquals(new JudgmentKeyword(courtType, phrase), foundKeyword);
        
               
    }
    
    
    @Test
    public void testGetOrCreateCcJudgmentKeyword_Get() {
        
        // given
        
        String phrase = keyword.getPhrase();
        CourtType courtType = CourtType.COMMON;
        
        
        // execute
        
        JudgmentKeyword foundKeyword = keywordCreator.getOrCreateJudgmentKeyword(courtType, phrase);
        
        
        // assert
        
        verify(judgmentKeywordRepository, never()).save(Mockito.any(JudgmentKeyword.class));
        verify(judgmentKeywordRepository).findOneByCourtTypeAndPhraseIgnoreCase(courtType, phrase);
        
        assertEquals(phrase, foundKeyword.getPhrase());
        assertTrue(foundKeyword == keyword);
        
               
    }
    

}
