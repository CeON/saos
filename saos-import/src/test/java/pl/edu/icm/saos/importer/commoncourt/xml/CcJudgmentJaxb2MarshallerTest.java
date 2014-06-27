package pl.edu.icm.saos.importer.commoncourt.xml;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import pl.edu.icm.saos.importer.commoncourt.xml.SourceCcJudgment;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class CcJudgmentJaxb2MarshallerTest {
    
    private static Logger log = LoggerFactory.getLogger(CcJudgmentJaxb2MarshallerTest.class);
    
    
    private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    
    @Before
    public void before() {
        marshaller.setClassesToBeBound(SourceCcJudgment.class);
    }
    
    String judgmentXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                            "<judgement id=\"155000000001521_III_AUa_001639_2011_Uz_2012-01-26_001\">" +
                                 "<signature>III AUa 1639/11</signature>" +
                                 "<date>2012-01-26 00:00:00.0 CET</date>" +
                                 "<publicationDate>2013-04-12 01:01:05.0 CEST</publicationDate>"+
                                 "<courtId>15500000</courtId>"+
                                 "<departmentId>1521</departmentId>"+
                                 "<type>SENTENCE, REASON</type>"+
                                 "<chairman>Barbara Ciuraszkiewicz  </chairman>"+
                                 "<judges>"+
                                 "   <judge> Barbara Ciuraszkiewicz</judge>"+
                                 "   <judge>Danuta Owsiana</judge>"+
                                 "   <judge>Danuta Rychlik-Dobrowolska</judge>"+
                                 "</judges>"+
                                 "<themePhrases>"+
                                 "   <themePhrase>Emerytura Wcześniejsza</themePhrase>"+
                                 "   <themePhrase>Emerytura</themePhrase>"+
                                 "</themePhrases>"+
                                 "<references>"+
                                 "   <reference>Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego (Dz. U. z 1964 r. Nr 43</reference>"+
                                 "   <reference>Ustawa z dnia 26 stycznia 1982 r. Karta Nauczyciela (Dz. U. z 1982 r. Nr 3, poz. 19 - art.</reference>"+
                                 "   <reference>Ustawa z dnia 17 grudnia 1998 r. o emeryturach i rentach z Funduszu Ubezpieczeń Społecznych</reference>"+
                                 "</references>"+
                                 "<legalBases>"+
                                 "   <legalBasis>art. 88 ust. 1 ustawy z dnia 26 stycznia 1982 r. Karta Nauczyciela</legalBasis>"+
                                 "</legalBases>"+
                                 "<recorder>Magdalena Krucka</recorder>"+
                                 "<decision></decision>"+
                                 "<reviser>Katarzyna Gulanowska</reviser>"+
                                 "<publisher>Katarzyna Gulanowska</publisher>"+
                                 "<dateOfPublication>2013-04-12 01:01:05.0 CEST</dateOfPublication>"+
                            "</judgement>";
    
    
    @Test
    public void marshall() {
        SourceCcJudgment judgment = (SourceCcJudgment)marshaller.unmarshal(new StreamSource(new StringReader(judgmentXml)));
        assertEquals("155000000001521_III_AUa_001639_2011_Uz_2012-01-26_001", judgment.getId());
        assertEquals("III AUa 1639/11", judgment.getSignature());
        assertEquals(new LocalDate(2012,1,26), judgment.getJudgmentDate());
        assertEquals(new LocalDateTime(2013,04,12,01,01,05).toDateTime(DateTimeZone.forID("CET")), judgment.getPublicationDate());
        assertEquals("15500000", judgment.getCourtId());
        assertEquals("1521", judgment.getDepartmentId());
        assertEquals(Lists.newArrayList("SENTENCE", "REASON"), judgment.getTypes());
        assertEquals("Barbara Ciuraszkiewicz", judgment.getChairman());
        assertEquals(Lists.newArrayList("Barbara Ciuraszkiewicz", "Danuta Owsiana", "Danuta Rychlik-Dobrowolska"), judgment.getJudges());
        assertEquals(Lists.newArrayList("Emerytura Wcześniejsza", "Emerytura"), judgment.getThemePhrases());
        assertEquals(Lists.newArrayList("Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego (Dz. U. z 1964 r. Nr 43", 
                                        "Ustawa z dnia 26 stycznia 1982 r. Karta Nauczyciela (Dz. U. z 1982 r. Nr 3, poz. 19 - art.",
                                        "Ustawa z dnia 17 grudnia 1998 r. o emeryturach i rentach z Funduszu Ubezpieczeń Społecznych"), judgment.getReferences());
        assertEquals(Lists.newArrayList("art. 88 ust. 1 ustawy z dnia 26 stycznia 1982 r. Karta Nauczyciela"), judgment.getLegalBases());
        assertEquals("Magdalena Krucka", judgment.getRecorder());
        assertEquals("", judgment.getDecision());
        assertEquals("Katarzyna Gulanowska", judgment.getReviser());
        assertEquals("Katarzyna Gulanowska", judgment.getPublisher());
        log.info("\n{}",judgment);
    }
}
