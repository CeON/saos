package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment.SourceCtDissentingOpinion;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class SourceCtJudgmentExtractorTest {

    private SourceCtJudgmentExtractor judgmentExtractor = new SourceCtJudgmentExtractor();
    
    
    private SourceCtJudgment sJudgment = new SourceCtJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void createNewJudgment() {
        ConstitutionalTribunalJudgment ctJudgment = judgmentExtractor.createNewJudgment();
        assertNotNull(ctJudgment);
    }
    
    
    @Test
    public void extractCourtCases() {
        
        sJudgment.setCaseNumber("CASE 211w/121");
        
        List<CourtCase> courtCases = judgmentExtractor.extractCourtCases(sJudgment, correctionList);
        
        assertEquals(1, courtCases.size());
        assertEquals(sJudgment.getCaseNumber(), courtCases.get(0).getCaseNumber());
        
    }

    
    @Test
    public void extractJudgmentType() {
        
        sJudgment.setJudgmentType("SENTENCE");
        
        JudgmentType retJudgmentType = judgmentExtractor.extractJudgmentType(sJudgment, correctionList);
        
        assertEquals(JudgmentType.SENTENCE, retJudgmentType);
        
    }
    
    
    @Test
    public void convertSpecific_DISSENTING_OPINIONS() {
        
        // given
        
        ConstitutionalTribunalJudgment ctJudgment = new ConstitutionalTribunalJudgment();
        
        SourceCtDissentingOpinion sDissentingOpinion1 = new SourceCtDissentingOpinion();
        sDissentingOpinion1.setAuthors(Lists.newArrayList("Jan Kowalski", "Adam Nowak"));
        sDissentingOpinion1.setTextContent("text content 1");
        
        SourceCtDissentingOpinion sDissentingOpinion2 = new SourceCtDissentingOpinion();
        sDissentingOpinion2.setAuthors(Lists.newArrayList("Jacek Zieliński"));
        sDissentingOpinion2.setTextContent("text content 2");
        
        sJudgment.setDissentingOpinions(Lists.newArrayList(sDissentingOpinion1, sDissentingOpinion2));
        
        
        // execute
        
        judgmentExtractor.convertSpecific(ctJudgment, sJudgment, correctionList);
        
        
        // assert
        
        ConstitutionalTribunalJudgmentDissentingOpinion expectedDissentingOpinion1 = new ConstitutionalTribunalJudgmentDissentingOpinion();
        expectedDissentingOpinion1.addAuthor(sDissentingOpinion1.getAuthors().get(0));
        expectedDissentingOpinion1.addAuthor(sDissentingOpinion1.getAuthors().get(1));
        expectedDissentingOpinion1.setTextContent(sDissentingOpinion1.getTextContent());
        expectedDissentingOpinion1.setJudgment(ctJudgment);
        
        ConstitutionalTribunalJudgmentDissentingOpinion expectedDissentingOpinion2 = new ConstitutionalTribunalJudgmentDissentingOpinion();
        expectedDissentingOpinion2.addAuthor(sDissentingOpinion2.getAuthors().get(0));
        expectedDissentingOpinion2.setTextContent(sDissentingOpinion2.getTextContent());
        expectedDissentingOpinion2.setJudgment(ctJudgment);
        
        assertThat(ctJudgment.getDissentingOpinions(), containsInAnyOrder(expectedDissentingOpinion1, expectedDissentingOpinion2));
    }
}
