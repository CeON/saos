package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.validation.ValidationException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.importer.common.ImportDateTimeFormatter;
import pl.edu.icm.saos.importer.notapi.common.DateTimeDeserializer;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.Source;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.SourceJudge;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class SourceScJudgmentParserTest {

    
    private JsonStringParser<SourceScJudgment> sourceScJudgmentParser = new JsonStringParser<>(SourceScJudgment.class);
    
    @Mock
    private ImportDateTimeFormatter dateTimeFormatter;
    
    @Mock
    private CommonValidator commonValidator;
    
    
    private static String jsonContent = "{textContent:'Wyrok z dnia 22 lutego 1994 r.', /* comment should pass */ "+
            "judges:[" 
                + "{name:'Józef Iwulski',function:null,specialRoles:['REPORTING_JUDGE']}, "
                + "{name:'Jacek Hero',function:'SSN',specialRoles:['REPORTING_JUDGE', 'XXX']}]," 
            + "source:"
                + "{sourceJudgmentPdfMD5:'16f3a55e22605d8b8fe26f42de45ba9c',"
                + "sourceJudgmentHtmlMD5:'220dfd859f5f6a57c8ea7739a531cd42',"
                + "sourceCode:'SUPREME_COURT',sourceJudgmentId:'Orzeczenia1&3042',"
                + "sourceJudgmentUrl:'http://www.sn.pl/orzecznictwo/SitePages/Baza%20orzecze%C5%84.aspx?ItemID=3042&ListName=Orzeczenia1',"
                + "publicationDateTime:'2014-02-11 11:43'},"
            + "caseNumber:'I PRN 5/94',"
            + "judgmentDate:'1994-02-22',"
            + "supremeCourtJudgmentForm:'wyrok SN',"
            + "personnelType:null,"
            + "receiptDate : '2001-01-04',"
            + "meansOfAppeal : 'kasacja',"
            + "judgmentResult : 'uchylono i przekazano do ponownego rozpoznania',"
            + "lowerCourtJudgments : [ 'II Kws 5/00 - wyrok z dnia 13 marca 2000 r. - Sąd Rejonowy Strzelce Krajeńskie', '(Kw 141/99) Kw 141/99 - orzeczenie z dnia 26 stycznia 2000 r. - Kolegium d/s Wykroczeń przy SR w Strzelcach Krajeńskich' ],"
            + "supremeCourtChambers:['Izba Administracyjna, Pracy i Ubezpieczeń Społecznych'],"
            + "supremeCourtChamberDivision:'Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych Wydział I'}";

    

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        jsonContent = JsonNormalizer.normalizeJson(jsonContent);
        
        sourceScJudgmentParser.setJsonFactory(new MappingJsonFactory());
        DateTimeDeserializer.setImportDateTimeFormatter(dateTimeFormatter);
        
        sourceScJudgmentParser.setCommonValidator(commonValidator);
        
    }
    
    
    @Test
    public void parse() throws JsonParseException {
        
        // given
        
        DateTime dateTime = new DateTime(2014, 2, 11, 11, 43);
        Mockito.when(dateTimeFormatter.parse(Mockito.eq("2014-02-11 11:43"))).thenReturn(dateTime);
        
        
        // execute
        
        SourceScJudgment sourceScJudgment = sourceScJudgmentParser.parseAndValidate(jsonContent);
        
        
        // assert
        
        assertEquals("Wyrok z dnia 22 lutego 1994 r.", sourceScJudgment.getTextContent());
        assertEquals("I PRN 5/94", sourceScJudgment.getCaseNumber());
        assertEquals(new LocalDate(1994, 2, 22), sourceScJudgment.getJudgmentDate());
        assertEquals("wyrok SN", sourceScJudgment.getSupremeCourtJudgmentForm());
        assertNull(sourceScJudgment.getPersonnelType());
        assertEquals(Lists.newArrayList("Izba Administracyjna, Pracy i Ubezpieczeń Społecznych"), sourceScJudgment.getSupremeCourtChambers());
        assertEquals("Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych Wydział I", sourceScJudgment.getSupremeCourtChamberDivision());
        
        assertEquals("uchylono i przekazano do ponownego rozpoznania", sourceScJudgment.getJudgmentResult());
        assertEquals("kasacja", sourceScJudgment.getMeansOfAppeal());
        assertEquals(new LocalDate(2001, 1, 4), sourceScJudgment.getReceiptDate());
        
        assertEquals(2, sourceScJudgment.getLowerCourtJudgments().size());
        assertEquals("II Kws 5/00 - wyrok z dnia 13 marca 2000 r. - Sąd Rejonowy Strzelce Krajeńskie", sourceScJudgment.getLowerCourtJudgments().get(0));
        assertEquals("(Kw 141/99) Kw 141/99 - orzeczenie z dnia 26 stycznia 2000 r. - Kolegium d/s Wykroczeń przy SR w Strzelcach Krajeńskich", sourceScJudgment.getLowerCourtJudgments().get(1));
        
        Source source = sourceScJudgment.getSource();
        assertEquals("Orzeczenia1&3042", source.getSourceJudgmentId());
        assertEquals("http://www.sn.pl/orzecznictwo/SitePages/Baza%20orzecze%C5%84.aspx?ItemID=3042&ListName=Orzeczenia1", source.getSourceJudgmentUrl());
        
        assertEquals(dateTime, source.getPublicationDateTime());
        
        
        assertEquals(2, sourceScJudgment.getJudges().size());
        
        SourceJudge judge = sourceScJudgment.getJudges().get(0);
        assertEquals("Józef Iwulski", judge.getName());
        assertNull(judge.getFunction());
        assertEquals(Lists.newArrayList("REPORTING_JUDGE"), judge.getSpecialRoles());
        
        judge = sourceScJudgment.getJudges().get(1);
        assertEquals("Jacek Hero", judge.getName());
        assertEquals("SSN", judge.getFunction());
        assertEquals(Lists.newArrayList("REPORTING_JUDGE", "XXX"), judge.getSpecialRoles());
        
    }
    
    
    @Test(expected=ValidationException.class)
    public void parse_JudgeNameNull() throws JsonParseException {
        
        // given
        
        DateTime dateTime = new DateTime(2014, 2, 11, 11, 43);
        Mockito.when(dateTimeFormatter.parse(Mockito.eq("2014-02-11 11:43"))).thenReturn(dateTime);
        
        Mockito.doThrow(ValidationException.class).when(commonValidator).validateEx(Mockito.any(SourceScJudgment.class));
        
        // execute
        
        sourceScJudgmentParser.parseAndValidate(jsonContent);
        
    }
    
    
    
}
