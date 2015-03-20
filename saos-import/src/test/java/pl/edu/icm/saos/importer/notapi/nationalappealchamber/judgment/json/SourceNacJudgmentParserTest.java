package pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.json;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;

import javax.validation.ValidationException;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.Source;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.SourceJudge;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class SourceNacJudgmentParserTest {

    private JsonStringParser<SourceNacJudgment> sourceNacJudgmentParser = new JsonStringParser<>(SourceNacJudgment.class);
    
    @Mock
    private CommonValidator commonValidator;


    private static String jsonContent = "{" 
            + "'textContent' : 'Sygn. akt: KIO/UZP 525/10 ...',"
            + "'judgmentType' : 'SENTENCE',"
            + "'judgmentDate' : '2010-04-26',"
            + "'courtReporters' : [ 'Agata Dziuban' ],"
            + "'judges' : [ {"
                + "'name' : 'Małgorzata Stręciwilk',"
                + "'specialRoles' : [ 'PRESIDING_JUDGE' ]"
            + "}, {"
                + "'name' : 'Ewa Sikorska',"
                + "'specialRoles' : [ ]"
            + "}, {"
                + "'name' : 'Robert Skrzeszewski',"
                + "'specialRoles' : [ ]"
            + "} ],"
            + "'caseNumber' : [ 'KIO/UZP 525/10', 'KIO/UZP 551/10' ],"
            + "'source' : {"
                + "'sourceJudgmentPdfMD5' : '9112dc5dcd793b50115abd39bcdd4e74',"
                + "'sourceJudgmentId' : '9112dc5dcd793b50115abd39bcdd4e74',"
                + "'sourceJudgmentUrl' : 'ftp://ftp.uzp.gov.pl/KIO/Wyroki/2010_0525__551.pdf',"
                + "'sourceCode' : 'NATIONAL_APPEALS_CHAMBER'"
            + "} }";
    
    
    @Before
    public void before() {

        jsonContent = JsonNormalizer.normalizeJson(jsonContent);
        
        sourceNacJudgmentParser.setJsonFactory(new MappingJsonFactory());
        
        sourceNacJudgmentParser.setCommonValidator(commonValidator);
        
    }
    

    //------------------------ TESTS --------------------------
    
    @Test
    public void parse() throws JsonParseException {
        
        // execute
        
        SourceNacJudgment sourceNacJudgment = sourceNacJudgmentParser.parseAndValidate(jsonContent);
        
        
        // assert
        assertEquals("Sygn. akt: KIO/UZP 525/10 ...", sourceNacJudgment.getTextContent());
        assertEquals("SENTENCE", sourceNacJudgment.getJudgmentType());
        assertEquals(new LocalDate(2010, 4, 26), sourceNacJudgment.getJudgmentDate());
        assertEquals(Lists.newArrayList("Agata Dziuban"), sourceNacJudgment.getCourtReporters());
        
        
        assertEquals(3, sourceNacJudgment.getJudges().size());
        
        SourceJudge firstJudge = sourceNacJudgment.getJudges().get(0);
        assertEquals("Małgorzata Stręciwilk", firstJudge.getName());
        assertEquals(Lists.newArrayList("PRESIDING_JUDGE"), firstJudge.getSpecialRoles());
        
        SourceJudge secondJudge = sourceNacJudgment.getJudges().get(1);
        assertEquals("Ewa Sikorska", secondJudge.getName());
        assertEquals(Lists.newArrayList(), secondJudge.getSpecialRoles());
        
        SourceJudge thirdJudge = sourceNacJudgment.getJudges().get(2);
        assertEquals("Robert Skrzeszewski", thirdJudge.getName());
        assertEquals(Lists.newArrayList(), thirdJudge.getSpecialRoles());
        
        
        assertEquals(Lists.newArrayList("KIO/UZP 525/10", "KIO/UZP 551/10"), sourceNacJudgment.getCaseNumbers());
        
        Source source = sourceNacJudgment.getSource();
        assertEquals("9112dc5dcd793b50115abd39bcdd4e74", source.getSourceJudgmentId());
        assertEquals("ftp://ftp.uzp.gov.pl/KIO/Wyroki/2010_0525__551.pdf", source.getSourceJudgmentUrl());
        
    }
    
    @Test(expected=ValidationException.class)
    public void parse_INVALID() throws JsonParseException {
        
        // given
        
        doThrow(ValidationException.class).when(commonValidator).validateEx(any(SourceNacJudgment.class));
        
        
        // execute
        
        sourceNacJudgmentParser.parseAndValidate(jsonContent);
    }
}
