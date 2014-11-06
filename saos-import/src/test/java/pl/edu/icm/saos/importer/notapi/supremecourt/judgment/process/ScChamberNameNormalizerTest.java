package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class ScChamberNameNormalizerTest {

    private ScChamberNameNormalizer scChamberNameNormalizer = new ScChamberNameNormalizer();
    
    
    @Test
    public void normalize_NO_CHANGE() {
        
        String normalizedName = scChamberNameNormalizer.normalize("Izba Karna");
        
        assertEquals("Izba Karna", normalizedName);
        
    }
    
    
    @Test
    public void normalize_CHANGE_IZBA_ADM() {
        
        String normalizedName = scChamberNameNormalizer.normalize("Izba Administracyjna, Pracy i Ubezpieczeń Społecznych");
        
        assertEquals("Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych", normalizedName);
        
    }
    
    

    @Test
    public void isChangedByNormalization_TRUE() {
        
        assertTrue(scChamberNameNormalizer.isChangedByNormalization("Izba Administracyjna, Pracy i Ubezpieczeń Społecznych "));
        
    }

    @Test
    public void isChangedByNormalization_FALSE() {
        
        assertFalse(scChamberNameNormalizer.isChangedByNormalization("Izba Cywilna"));
        
    }

}
