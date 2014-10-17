package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;
import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class ScJudgmentFormCreatorTest {

    private ScJudgmentFormCreator scJudgmentFormCreator = new ScJudgmentFormCreator();
    
    private ScJudgmentFormRepository scJudgmentFormRepository = mock(ScJudgmentFormRepository.class);
    
    
    @Before
    public void before() {
        scJudgmentFormCreator.setScJudgmentFormRepository(scJudgmentFormRepository);
    }
    
    
    @Test
    public void getOrCreateScJudgmentForm_FOUND() {
        
        // given
        
        String judgmentFormName = "wyrok";
        SupremeCourtJudgmentForm scJudgmentForm = new SupremeCourtJudgmentForm();
        
        when(scJudgmentFormRepository.findOneByName(judgmentFormName)).thenReturn(scJudgmentForm);
        
        
        // execute
        
        SupremeCourtJudgmentForm retScJudgmentForm = scJudgmentFormCreator.getOrCreateScJudgmentForm(judgmentFormName);
        
        
        // assert
        
        assertTrue(scJudgmentForm == retScJudgmentForm);
        verify(scJudgmentFormRepository).findOneByName(judgmentFormName);
        verifyNoMoreInteractions(scJudgmentFormRepository);
        
    }
    
    
    @Test
    public void getOrCreateScJudgmentForm_CREATED() {
        
        // given
        
        String judgmentFormName = "wyrok";
        
        when(scJudgmentFormRepository.findOneByName(judgmentFormName)).thenReturn(null);
        
        
        // execute
        
        SupremeCourtJudgmentForm retScJudgmentForm = scJudgmentFormCreator.getOrCreateScJudgmentForm(judgmentFormName);
        
        
        // assert
        
        assertNotNull(retScJudgmentForm);
        assertEquals(judgmentFormName, retScJudgmentForm.getName());
        
        verify(scJudgmentFormRepository).findOneByName(judgmentFormName);
        verify(scJudgmentFormRepository).saveAndFlush(retScJudgmentForm);
        verifyNoMoreInteractions(scJudgmentFormRepository);
        
    }
    
}
