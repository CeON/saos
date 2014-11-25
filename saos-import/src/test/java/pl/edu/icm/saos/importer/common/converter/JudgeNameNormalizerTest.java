package pl.edu.icm.saos.importer.common.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgeNameNormalizerTest {

    
    private JudgeNameNormalizer judgeNameNormalizer = new JudgeNameNormalizer();
    
    
    {
        
        judgeNameNormalizer.postConstruct();
        
    }
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void normalize_sedzia() {
        
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("Sędzia Jan \t Sędzia Nowak Sędzia "));
        
    }
    
    @Test
    public void normalize_sedzia_sadu_okregowego() {
        
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("Sędzia Sądu Okręgowego Jan \t Sędzia Nowak Sędzia "));
        
    }
    
    @Test
    public void normalize_del() {
        
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("del/ Jan Nowak "));
        
    }
    
    
    @Test
    public void normalize_sa() {
        
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("SA Jan Nowak "));
        
    }
    
    @Test
    public void normalize_nonAlphabetic() {
        
        assertEquals("Jan Łowak", judgeNameNormalizer.normalize("SA: Jan Łowak () "));
        
    }
    
    @Test
    public void normalize_dashAtTheEnds() {
        
        assertEquals("Jan Łowak", judgeNameNormalizer.normalize("- Jan Łowak - "));
        
    }
    
    @Test
    public void normalize_dotAtTheEnds() {
        
        assertEquals("Jan Łowak", judgeNameNormalizer.normalize(". Jan Łowak."));
        
    }
    
    
    @Test
    public void normalize_partWithDots() {
        
        assertEquals("Jan Łowak", judgeNameNormalizer.normalize(". Jan Łowak SA."));
        
    }
    
    @Test
    public void normalize_notChanged() {
        
        assertEquals("Władysława Władzińska", judgeNameNormalizer.normalize("Władysława Władzińska"));
        
    }
    
    @Test
    public void normalize_notChanged_oneLetterAsFName() {
        
        assertEquals("W. Władzińska", judgeNameNormalizer.normalize("W. Władzińska"));
        
    }
    
    @Test
    public void normalize_part_withDash() {
        
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("SA- Jan Nowak "));
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("SA - Jan Nowak "));
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("SA- Jan Nowak "));

    }
    
    
    @Test
    public void normalize_part_withDots() {
        
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("SA. Jan Nowak "));
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("SA . Jan Nowak "));
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("Jan Nowak SA. "));

    }
    
    
    @Test
    public void normalize_onlyNonAlphabetic() {
        
        assertEquals("", judgeNameNormalizer.normalize("!222"));

    }
    
    
    @Test
    public void normalize_wrongPartInTheMiddle() {
        
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("Jan ppłk Nowak "));
        
    }
    
    
    @Test
    public void normalize_hard() {
        
        assertEquals("J. Stanisław Łowak", judgeNameNormalizer.normalize("gen. sn J. Stanisław Łowak-spr"));
        assertEquals("J. Stanisław Łowak", judgeNameNormalizer.normalize("SR (del.) J. Stanisław Łowak (Sprawozdawca)"));
        assertEquals("Hanna Witkowska-Zalewska", judgeNameNormalizer.normalize("w w Gdańsku Hanna Witkowska-Zalewska"));
        assertEquals("Andrzej Tomczyk", judgeNameNormalizer.normalize("Sędzia WSO del. do SN ppłk Andrzej Tomczyk"));
        
    }
    
    
}
