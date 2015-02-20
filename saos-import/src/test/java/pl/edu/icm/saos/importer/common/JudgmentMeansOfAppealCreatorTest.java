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
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
import pl.edu.icm.saos.persistence.repository.MeansOfAppealRepository;

/**
 * @author madryk
 */
public class JudgmentMeansOfAppealCreatorTest {

    private JudgmentMeansOfAppealCreator meansOfAppealCreator = new JudgmentMeansOfAppealCreator();
    
    private MeansOfAppealRepository meansOfAppealRepository = mock(MeansOfAppealRepository.class);
    
    
    @Before
    public void setUp() {
        meansOfAppealCreator.setMeansOfAppealRepository(meansOfAppealRepository);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void fetchOrCreateMeansOfAppeal_FETCH() {

        // given
        MeansOfAppeal meansOfAppeal = new MeansOfAppeal(CourtType.SUPREME, "text");
        
        when(meansOfAppealRepository.findOneByCourtTypeAndNameIgnoreCase(meansOfAppeal.getCourtType(), meansOfAppeal.getName()))
                .thenReturn(meansOfAppeal);
        
        
        // execute
        MeansOfAppeal retMeansOfAppeal = meansOfAppealCreator.fetchOrCreateMeansOfAppeal(meansOfAppeal.getCourtType(), meansOfAppeal.getName());
        
        
        // assert
        assertTrue(meansOfAppeal == retMeansOfAppeal);
        
        verify(meansOfAppealRepository).findOneByCourtTypeAndNameIgnoreCase(meansOfAppeal.getCourtType(), meansOfAppeal.getName());
        verifyNoMoreInteractions(meansOfAppealRepository);
    }
    
    
    @Test
    public void fetchOrCreateMeansOfAppeal_CREATE() {
        
        // execute
        MeansOfAppeal retMeansOfAppeal = meansOfAppealCreator.fetchOrCreateMeansOfAppeal(CourtType.SUPREME, "text");
        
        
        // assert
        assertEquals(new MeansOfAppeal(CourtType.SUPREME, "text"), retMeansOfAppeal);
        
        verify(meansOfAppealRepository).findOneByCourtTypeAndNameIgnoreCase(CourtType.SUPREME, "text");
        verify(meansOfAppealRepository).save(new MeansOfAppeal(CourtType.SUPREME, "text"));
        verify(meansOfAppealRepository).flush();
        verifyNoMoreInteractions(meansOfAppealRepository);
    }
    
}
