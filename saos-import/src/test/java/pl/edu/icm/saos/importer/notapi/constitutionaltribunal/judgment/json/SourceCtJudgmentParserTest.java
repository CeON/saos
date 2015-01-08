package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;

import javax.validation.ValidationException;

import org.assertj.core.util.Lists;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment.Source;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment.SourceCtDissentingOpinion;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment.SourceCtJudge;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class SourceCtJudgmentParserTest {

    private JsonStringParser<SourceCtJudgment> sourceScJudgmentParser = new JsonStringParser<>(SourceCtJudgment.class);
    
    @Mock
    private CommonValidator commonValidator;
    
    
    private static String jsonContent = "{" +
            "'courtReporters' : [ 'Grażyna Szałygo' ],"
            + "'dissentingOpinions' : [ {"
                + "'textContent' : 'Zdanie odrębne Sędziów Trybunału Konstytucyjnego ...',"
                + "'authors' : [ 'Teresa Dębowska-Romanowska', 'Bohdan Zdziennicki' ]"
            + "} ],"
            + "'caseNumber' : 'P 25/02',"
            + "'judges' : [ {"
                + "'name' : 'Teresa Dębowska-Romanowska',"
                + "'specialRoles' : [ 'PRESIDING_JUDGE' ]"
            + "}, {"
                + "'name' : 'Mirosław Wyrzykowski',"
                + "'specialRoles' : [ 'REPORTING_JUDGE' ]"
                + "}, {"
                + "'name' : 'Bohdan Zdziennicki',"
                + "'specialRoles' : [ ]"
                + "} ],"
            + "'source' : {"
                + "'sourceJudgmentDocMD5' : '3b42a6299303c65d869c4806fdcdbf7a',"
                + "'sourceJudgmentId' : '3b42a6299303c65d869c4806fdcdbf7a',"
                + "'sourceJudgmentUrl' : 'http://otk.trybunal.gov.pl/orzeczenia/teksty/otk/2005/P_25_02.doc',"
                + "'sourceCode' : 'CONSTITUTIONAL_TRIBUNAL'"
            + "},"
            + "'judgmentDate' : '2005-06-21',"
            + "'textContent' : '65/6/A/2005 WYROK z dnia 21 czerwca 2005 ...',"
            + "'judgmentType' : 'SENTENCE'"
            + "}";
    
    
    @Before
    public void before() {

        jsonContent = JsonNormalizer.normalizeJson(jsonContent);
        
        sourceScJudgmentParser.setJsonFactory(new MappingJsonFactory());
        
        sourceScJudgmentParser.setCommonValidator(commonValidator);
        
    }
    

    //------------------------ TESTS --------------------------
    
    @Test
    public void parse() throws JsonParseException {
        
        // execute
        
        SourceCtJudgment sourceScJudgment = sourceScJudgmentParser.parseAndValidate(jsonContent);
        
        
        // assert
        assertEquals(Lists.newArrayList("Grażyna Szałygo"), sourceScJudgment.getCourtReporters());
        assertEquals("P 25/02", sourceScJudgment.getCaseNumber());
        
        
        assertEquals(1, sourceScJudgment.getDissentingOpinions().size());
        
        SourceCtDissentingOpinion dissentingOpinion = sourceScJudgment.getDissentingOpinions().get(0);
        assertEquals("Zdanie odrębne Sędziów Trybunału Konstytucyjnego ...", dissentingOpinion.getTextContent());
        assertEquals(Lists.newArrayList("Teresa Dębowska-Romanowska", "Bohdan Zdziennicki"), dissentingOpinion.getAuthors());
        
        
        assertEquals(3, sourceScJudgment.getJudges().size());
        
        SourceCtJudge firstJudge = sourceScJudgment.getJudges().get(0);
        assertEquals("Teresa Dębowska-Romanowska", firstJudge.getName());
        assertEquals(Lists.newArrayList("PRESIDING_JUDGE"), firstJudge.getSpecialRoles());
        
        SourceCtJudge secondJudge = sourceScJudgment.getJudges().get(1);
        assertEquals("Mirosław Wyrzykowski", secondJudge.getName());
        assertEquals(Lists.newArrayList("REPORTING_JUDGE"), secondJudge.getSpecialRoles());
        
        SourceCtJudge thirdJudge = sourceScJudgment.getJudges().get(2);
        assertEquals("Bohdan Zdziennicki", thirdJudge.getName());
        assertEquals(Lists.newArrayList(), thirdJudge.getSpecialRoles());
        
        
        Source source = sourceScJudgment.getSource();
        assertEquals("3b42a6299303c65d869c4806fdcdbf7a", source.getSourceJudgmentId());
        assertEquals("http://otk.trybunal.gov.pl/orzeczenia/teksty/otk/2005/P_25_02.doc", source.getSourceJudgmentUrl());
        
        
        assertEquals(new LocalDate(2005, 6, 21), sourceScJudgment.getJudgmentDate());
        assertEquals("65/6/A/2005 WYROK z dnia 21 czerwca 2005 ...", sourceScJudgment.getTextContent());
        assertEquals("SENTENCE", sourceScJudgment.getJudgmentType());
        
    }
    
    @Test(expected=ValidationException.class)
    public void parse_INVALID() throws JsonParseException {
        
        // given
        
        doThrow(ValidationException.class).when(commonValidator).validateEx(any(SourceCtJudgment.class));
        
        
        // execute
        
        sourceScJudgmentParser.parseAndValidate(jsonContent);
    }
}
