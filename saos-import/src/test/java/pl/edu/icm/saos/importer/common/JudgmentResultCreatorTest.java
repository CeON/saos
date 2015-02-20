package pl.edu.icm.saos.importer.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.repository.JudgmentResultRepository;

/**
 * @author madryk
 */
public class JudgmentResultCreatorTest {

    private JudgmentResultCreator judgmentResultCreator = new JudgmentResultCreator();
    
    private JudgmentResultRepository judgmentResultRepository = mock(JudgmentResultRepository.class);
    
    
    @Before
    public void setUp() {
        judgmentResultCreator.setJudgmentResultRepository(judgmentResultRepository);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void fetchOrCreateJudgmentResult_FETCH() {
        
        // given
        JudgmentResult judgmentResult = new JudgmentResult(CourtType.SUPREME, "text");
        
        when(judgmentResultRepository.findOneByCourtTypeAndTextIgnoreCase(judgmentResult.getCourtType(), judgmentResult.getText()))
                .thenReturn(judgmentResult);
        
        
        // execute
        JudgmentResult retJudgmentResult = judgmentResultCreator.fetchOrCreateJudgmentResult(judgmentResult.getCourtType(), judgmentResult.getText());
        
        
        // assert
        assertTrue(judgmentResult == retJudgmentResult);
        
        verify(judgmentResultRepository).findOneByCourtTypeAndTextIgnoreCase(judgmentResult.getCourtType(), judgmentResult.getText());
        verifyNoMoreInteractions(judgmentResultRepository);
    }
    
    
    @Test
    public void fetchOrCreateJudgmentResult_CREATE() {
        
        // execute
        JudgmentResult retJudgmentResult = judgmentResultCreator.fetchOrCreateJudgmentResult(CourtType.SUPREME, "text");
        
        
        // assert
        assertEquals(new JudgmentResult(CourtType.SUPREME, "text"), retJudgmentResult);
        
        verify(judgmentResultRepository).findOneByCourtTypeAndTextIgnoreCase(CourtType.SUPREME, "text");
        verify(judgmentResultRepository).save(new JudgmentResult(CourtType.SUPREME, "text"));
        verify(judgmentResultRepository).flush();
        verifyNoMoreInteractions(judgmentResultRepository);
    }
    
}
