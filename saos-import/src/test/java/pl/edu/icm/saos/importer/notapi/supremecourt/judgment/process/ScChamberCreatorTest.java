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

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class ScChamberCreatorTest {

    private ScChamberCreator scChamberCreator = new ScChamberCreator();
    
    private ScChamberRepository scChamberRepository = mock(ScChamberRepository.class);
    
    
    
    @Before
    public void before() {
        
        scChamberCreator.setScChamberRepository(scChamberRepository);
        
    }
    
    
    @Test
    public void getOrCreateScChamber_Found() {
        
        // given 
        
        String chamberName = "chamber name";
        
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        when(scChamberRepository.findOneByName(chamberName)).thenReturn(scChamber);
        
        
        // execute
        
        SupremeCourtChamber retScChamber = scChamberCreator.getOrCreateScChamber(chamberName);
        
        
        // assert
        
        assertTrue(retScChamber == scChamber);
        
        verify(scChamberRepository).findOneByName(chamberName);
        verifyNoMoreInteractions(scChamberRepository);
    }
   
    
    
    
    @Test
    public void getOrCreateScChamber_NotFound_Create() {
        
        // given 
        
        String chamberName = "chamber name";
        
        when(scChamberRepository.findOneByName(chamberName)).thenReturn(null);
        
        
        // execute
        
        SupremeCourtChamber retScChamber = scChamberCreator.getOrCreateScChamber(chamberName);
        
        
        // assert
        
        assertNotNull(retScChamber);
        assertEquals(chamberName, retScChamber.getName());
        
        verify(scChamberRepository).findOneByName(chamberName);
        verify(scChamberRepository).saveAndFlush(retScChamber);
        verifyNoMoreInteractions(scChamberRepository);
        
    }
}
