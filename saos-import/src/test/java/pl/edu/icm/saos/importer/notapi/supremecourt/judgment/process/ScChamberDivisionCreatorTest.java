package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class ScChamberDivisionCreatorTest {

    
    private ScChamberDivisionCreator scChamberDivisionCreator = new ScChamberDivisionCreator();
    
    @Mock private ScChamberRepository scChamberRepository;
    
    @Mock private ScChamberDivisionRepository scChamberDivisionRepository;

    @Mock private ScChamberDivisionNameExtractor scChamberDivisionNameExtractor;

    
    @Before
    public void before() {
    
        MockitoAnnotations.initMocks(this);
        
        scChamberDivisionCreator.setScChamberDivisionNameExtractor(scChamberDivisionNameExtractor);
        scChamberDivisionCreator.setScChamberDivisionRepository(scChamberDivisionRepository);
        scChamberDivisionCreator.setScChamberRepository(scChamberRepository);
        
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void getOrCreateScChamberDivision_EmptyDivisioName() {
        
        scChamberDivisionCreator.getOrCreateScChamberDivision("");
        
    } 
    
    
    
    @Test
    public void getOrCreateScChamberDivision_Found() {
        
        // given 
        
        String divisionFullName = "Izba Karna Wydział 2";
        SupremeCourtChamberDivision scChamberDivision = new SupremeCourtChamberDivision();
        when(scChamberDivisionRepository.findOneByFullName(divisionFullName)).thenReturn(scChamberDivision);
        
        
        //execute
        
        SupremeCourtChamberDivision retScChamberDivision = scChamberDivisionCreator.getOrCreateScChamberDivision(divisionFullName);
        
        
        // assert
        
        assertTrue(retScChamberDivision == scChamberDivision);
        
    } 
    
    
    @Test(expected=EntityNotFoundException.class)
    public void getOrCreateScChamberDivision_NotFound_Create_ChamberNotFound() {
        
        // given 
        
        String divisionFullName = "Izba Karna Wydział 2";
        when(scChamberDivisionRepository.findOneByFullName(divisionFullName)).thenReturn(null);
        
        String chamberName = "Izba Karna";
        when(scChamberDivisionNameExtractor.extractChamberName(divisionFullName)).thenReturn(chamberName);
        when(scChamberRepository.findOneByName(chamberName)).thenReturn(null);
        
        
        //execute
        
        scChamberDivisionCreator.getOrCreateScChamberDivision(divisionFullName);
        
    } 
    
    
    
    @Test
    public void getOrCreateScChamberDivision_NotFound_Create_ChamberFound() {
        
        // given 
        
        String divisionFullName = "Izba Karna Wydział 2";
        when(scChamberDivisionRepository.findOneByFullName(divisionFullName)).thenReturn(null);
        
        String chamberName = "Izba Karna";
        when(scChamberDivisionNameExtractor.extractChamberName(divisionFullName)).thenReturn(chamberName);
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        when(scChamberRepository.findOneByName(chamberName)).thenReturn(scChamber);
        
        String divisionName = "Wydział 2";
        when(scChamberDivisionNameExtractor.extractDivisionName(divisionFullName)).thenReturn(divisionName);
        
        
        //execute
        
        SupremeCourtChamberDivision scChamberDivision = scChamberDivisionCreator.getOrCreateScChamberDivision(divisionFullName);
        
        
        // assert
        
        assertTrue(scChamber == scChamberDivision.getScChamber());
        assertEquals(divisionFullName, scChamberDivision.getFullName());
        assertEquals(divisionName, scChamberDivision.getName());
        
        verify(scChamberDivisionRepository).saveAndFlush(scChamberDivision);
        
    } 
    
}
