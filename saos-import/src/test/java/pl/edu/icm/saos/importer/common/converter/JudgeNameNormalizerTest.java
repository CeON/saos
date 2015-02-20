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
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void normalize_sedzia() {
        
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("Sędzia Jan \t Sędzia Nowak Sędzia "));
        
    }
    
    @Test
    public void normalize_sedzia_sadu_okregowego() {
        
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("Sędzia Sądu Okręgowego Jan \t Sędzia Nowak Sędzia "));
        assertEquals("Aleksandra Nowicka", judgeNameNormalizer.normalize("Sędzia SO Aleksandra Nowicka"));
        assertEquals("Elżbieta Gawryszczak", judgeNameNormalizer.normalize("Sędzia S O Elżbieta Gawryszczak"));
        assertEquals("Agnieszka Leżańska", judgeNameNormalizer.normalize("S. S.o Agnieszka Leżańska"));
        assertEquals("Tomasz Jaskłowski", judgeNameNormalizer.normalize("S S O Tomasz Jaskłowski"));
        assertEquals("Barbara Kubasik", judgeNameNormalizer.normalize("S.s.o Barbara Kubasik"));
        assertEquals("Hanna Matuszewska", judgeNameNormalizer.normalize("So. Hanna Matuszewska"));
        assertEquals("Barbara Frankowska", judgeNameNormalizer.normalize("Soo Barbara Frankowska"));
        assertEquals("Katarzyna Bojańczyk", judgeNameNormalizer.normalize("Sędzia S.O. Katarzyna Bojańczyk"));
        
    }
    
    @Test
    public void normalize_sedzia_sadu_apelacyjnego() {
        
        assertEquals("Bogdan Radomski", judgeNameNormalizer.normalize("Sędzia Sądu Apelacyjnego Bogdan Radomski"));
        assertEquals("Urszula Wiercińska", judgeNameNormalizer.normalize("Sędzia SA– Urszula Wiercińska"));
        assertEquals("Hanna Nowicka De Poraj", judgeNameNormalizer.normalize("Sędzia S.A. Hanna Nowicka de Poraj"));
        assertEquals("Grzegorz Krężołek", judgeNameNormalizer.normalize("Ssa Grzegorz Krężołek"));
    }
    
    @Test
    public void normalize_z_izby() {
        
        assertEquals("Andrzej Wróbel", judgeNameNormalizer.normalize("Andrzej Wróbel Z Izby Pracy"));
        assertEquals("Dariusz Zawistowski", judgeNameNormalizer.normalize("Dariusz Zawistowski Z Izby Cywilnej"));
    }
    
    @Test
    public void normalize_del() {
        
        assertEquals("Jan Nowak", judgeNameNormalizer.normalize("del/ Jan Nowak "));
        assertEquals("Aleksandra Kempczyńska", judgeNameNormalizer.normalize("del) – Aleksandra Kempczyńska"));
        assertEquals("Alicja Podlewska", judgeNameNormalizer.normalize("Deleg. Alicja Podlewska"));
        
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
        assertEquals("Paweł Szwedowski", judgeNameNormalizer.normalize("Paweł Szwedowski--"));
        
    }
    
    @Test
    public void normalize_dotAtTheEnds() {
        
        assertEquals("Jan Łowak", judgeNameNormalizer.normalize(". Jan Łowak."));
        assertEquals("Grażyna Tokarczyk", judgeNameNormalizer.normalize("Grażyna Tokarczyk.."));
        
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
    public void normalize_wrongPartAsWhole() {
        
        assertEquals("", judgeNameNormalizer.normalize("Sędziowie"));
        assertEquals("", judgeNameNormalizer.normalize("Sprawozdawca"));
        assertEquals("", judgeNameNormalizer.normalize(" Przewodniczący "));
    }
    
    
    @Test
    public void normalize_hard() {
        
        assertEquals("J. Stanisław Łowak", judgeNameNormalizer.normalize("gen. sn J. Stanisław Łowak-spr"));
        assertEquals("J. Stanisław Łowak", judgeNameNormalizer.normalize("SR (del.) J. Stanisław Łowak (Sprawozdawca)"));
        assertEquals("Hanna Witkowska-Zalewska", judgeNameNormalizer.normalize("w w Gdańsku Hanna Witkowska-Zalewska"));
        assertEquals("Andrzej Tomczyk", judgeNameNormalizer.normalize("Sędzia WSO del. do SN ppłk Andrzej Tomczyk"));
        
        assertEquals("Anna Pelc", judgeNameNormalizer.normalize("Anna Pelc (spr.)aw)"));
        assertEquals("Sylwia Kornatowicz", judgeNameNormalizer.normalize("Sylwia Kornatowicz (spr.)awozdanie)"));
        assertEquals("Daniel Błaszak", judgeNameNormalizer.normalize("W(del)  Daniel Błaszak-"));
        assertEquals("Regina Owczarek-Jędrasik", judgeNameNormalizer.normalize("Sędzia S A Regina Owczarek – Jędrasik / spraw./"));
        assertEquals("Bartosz Kaźmierak", judgeNameNormalizer.normalize("p.o. Sędziego Bartosz Kaźmierak"));
        assertEquals("Wiesław Żywolewski", judgeNameNormalizer.normalize("Wiesław Żywolewski (s. ref.)"));
        assertEquals("Urszula Sipińska-Sęk", judgeNameNormalizer.normalize("Sędzia S R () Urszula Sipińska-Sęk"));
        assertEquals("Izabela Bluszcz", judgeNameNormalizer.normalize("Protokolant Sekr. Sąd. Izabela Bluszcz"));
        assertEquals("Anna Hordyńska", judgeNameNormalizer.normalize("Anna Hordyńska – ref."));
        assertEquals("Szczęsny Szymański", judgeNameNormalizer.normalize("Szczęsny Szymański (s. ref)"));
        assertEquals("Adrianna Szewczyk-Kubat", judgeNameNormalizer.normalize("Adrianna Szewczyk –Kubat /ref/"));
        assertEquals("W. Damaszko", judgeNameNormalizer.normalize("Sędzia-W. Damaszko"));
        assertEquals("Barbara Baran", judgeNameNormalizer.normalize("Barbara Baran (deleg.) – spraw."));
        
        
    }
    
    
    @Test
    public void normalize_Blank() {
        
        assertEquals("", judgeNameNormalizer.normalize("         "));
        assertEquals("", judgeNameNormalizer.normalize(null));
    }
    
}
