package pl.edu.icm.saos.importer.commoncourt.court;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * @author Łukasz Dumiszewski
 */

public class XmlCommonCourtMarshallerTest {
    
    private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    
    @Before
    public void before() {
        marshaller.setClassesToBeBound(XmlCommonCourt.class);
    }
    
    private String ccXml = "<court id=\"15500000\">" +
                                "<model>" +
                                    "<name>Sąd Apelacyjny we Wrocławiu</name>" +
                                    "<abbrev>SAW</abbrev>" +
                                    "<domain>orzeczenia.wroclaw.sa.gov.pl</domain>" +
                                    "<departments>" +
                                        "<department id=\"0000503\">I Wydział Cywilny</department>" +
                                        "<department id=\"0001006\">II Wydział Karny</department>" +
                                        "<department id=\"0001521\">III Wydział Pracy i Ubezpieczeń Społecznych</department>" +
                                    "</departments>" +
                                 "</model>" +
                              "</court>";
    
    
    @Test
    public void unmarshall() {
        XmlCommonCourt court = (XmlCommonCourt)marshaller.unmarshal(new StreamSource(new StringReader(ccXml)));
        assertEquals("15500000", court.getId());
        assertEquals("Sąd Apelacyjny we Wrocławiu", court.getName());
        assertEquals("0000503", court.getDepartments().get(0).getId());
        assertEquals("I Wydział Cywilny", court.getDepartments().get(0).getName());
        assertEquals("0001006", court.getDepartments().get(1).getId());
        assertEquals("II Wydział Karny", court.getDepartments().get(1).getName());
        assertEquals("0001521", court.getDepartments().get(2).getId());
        assertEquals("III Wydział Pracy i Ubezpieczeń Społecznych", court.getDepartments().get(2).getName());
    }
    
    
}
