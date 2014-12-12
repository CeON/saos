package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.validation.ValidationException;

import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.importer.common.ImportDateTimeFormatter;
import pl.edu.icm.saos.importer.notapi.common.DateTimeDeserializer;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment.Source;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment.SourceScJudge;

import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author Łukasz Dumiszewski
 */

public class SourceScJudgmentParserTest {

    
    private JsonItemParser<SourceScJudgment> sourceScJudgmentParser = new JsonItemParser<>(SourceScJudgment.class);
    
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
    public void parse() {
        
        // given
        
        DateTime dateTime = new DateTime(2014, 2, 11, 11, 43);
        Mockito.when(dateTimeFormatter.parse(Mockito.eq("2014-02-11 11:43"))).thenReturn(dateTime);
        
        
        // execute
        
        SourceScJudgment sourceScJudgment = sourceScJudgmentParser.parse(jsonContent);
        
        
        // assert
        
        assertEquals("Wyrok z dnia 22 lutego 1994 r.", sourceScJudgment.getTextContent());
        assertEquals("I PRN 5/94", sourceScJudgment.getCaseNumber());
        assertEquals(new LocalDate(1994, 2, 22), sourceScJudgment.getJudgmentDate());
        assertEquals("wyrok SN", sourceScJudgment.getSupremeCourtJudgmentForm());
        assertNull(sourceScJudgment.getPersonnelType());
        assertEquals(Lists.newArrayList("Izba Administracyjna, Pracy i Ubezpieczeń Społecznych"), sourceScJudgment.getSupremeCourtChambers());
        assertEquals("Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych Wydział I", sourceScJudgment.getSupremeCourtChamberDivision());
        
        Source source = sourceScJudgment.getSource();
        assertEquals("Orzeczenia1&3042", source.getSourceJudgmentId());
        assertEquals("http://www.sn.pl/orzecznictwo/SitePages/Baza%20orzecze%C5%84.aspx?ItemID=3042&ListName=Orzeczenia1", source.getSourceJudgmentUrl());
        
        assertEquals(dateTime, source.getPublicationDateTime());
        
        
        assertEquals(2, sourceScJudgment.getJudges().size());
        
        SourceScJudge judge = sourceScJudgment.getJudges().get(0);
        assertEquals("Józef Iwulski", judge.getName());
        assertNull(judge.getFunction());
        assertEquals(Lists.newArrayList("REPORTING_JUDGE"), judge.getSpecialRoles());
        
        judge = sourceScJudgment.getJudges().get(1);
        assertEquals("Jacek Hero", judge.getName());
        assertEquals("SSN", judge.getFunction());
        assertEquals(Lists.newArrayList("REPORTING_JUDGE", "XXX"), judge.getSpecialRoles());
        
    }
    
    
    @Test(expected=ValidationException.class)
    public void parse_JudgeNameNull() {
        
        // given
        
        DateTime dateTime = new DateTime(2014, 2, 11, 11, 43);
        Mockito.when(dateTimeFormatter.parse(Mockito.eq("2014-02-11 11:43"))).thenReturn(dateTime);
        
        Mockito.doThrow(ValidationException.class).when(commonValidator).validateEx(Mockito.any(SourceScJudgment.class));
        
        // execute
        
        sourceScJudgmentParser.parse(jsonContent);
        
    }
    
    
    
}
