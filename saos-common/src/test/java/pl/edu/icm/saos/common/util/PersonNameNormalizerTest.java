package pl.edu.icm.saos.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class PersonNameNormalizerTest {
    
        
        
    @Test
    public void normalize_RemoveWhitespaces() {
            
        assertEquals("Jan Nowak", PersonNameNormalizer.normalize(" Jan \t Nowak "));
            
    }
        
    @Test
    public void normalize_CapitalizeFirstLetters() {
        
        assertEquals("Jan Nowak-Jeziorański Фчю", PersonNameNormalizer.normalize("Jan nowak-jeziorański фчю"));
        
    }
        
    @Test
    public void normalize_RemoveNonAlphabetic() {
        
        assertEquals("Jan Nowak-Jeziorański-Żak Фчюӿ", PersonNameNormalizer.normalize("%Jan (^)* Nowak-Jeziorański-Żak Фчюӿ !"));
        
    }
    
    
    @Test
    public void normalize_ReplaceLongDashWithShort() {
        
        assertEquals("Jan Nowak-Jeziorański", PersonNameNormalizer.normalize("Jan Nowak–Jeziorański"));
        
    }
    
    @Test
    public void unify() {
        assertEquals("jan nowak-jeziorański", PersonNameNormalizer.unify(" Jan  Nowak– Jeziorański "));
    }
  
}
