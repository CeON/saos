package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment.Source;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment.SourceScJudge;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class SourceScJudgmentExtractorTest {

    
    private SourceScJudgmentExtractor judgmentExtractor = new SourceScJudgmentExtractor();
    
    @Mock private ScJudgmentFormConverter scJudgmentFormConverter;
    
    @Mock private ScJudgmentFormNameNormalizer scJudgmentFormNameNormalizer;
    
    @Mock private ScChamberCreator scChamberCreator;
    
    @Mock private ScChamberDivisionCreator scChamberDivisionCreator;
    
    @Mock private ScChamberNameNormalizer scChamberNameNormalizer;
    
    @Mock private ScJudgmentFormCreator scJudgmentFormCreator;
    
    
    private SourceScJudgment sJudgment = new SourceScJudgment();
    
    
    
      
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        
        judgmentExtractor.setScChamberCreator(scChamberCreator);
        judgmentExtractor.setScChamberDivisionCreator(scChamberDivisionCreator);
        judgmentExtractor.setScChamberNameNormalizer(scChamberNameNormalizer);
        judgmentExtractor.setScJudgmentFormConverter(scJudgmentFormConverter);
        judgmentExtractor.setScJudgmentFormCreator(scJudgmentFormCreator);
        judgmentExtractor.setScJudgmentFormNameNormalizer(scJudgmentFormNameNormalizer);
    }

  

    @Test
    public void createNewJudgment() {
        SupremeCourtJudgment ccJudgment = judgmentExtractor.createNewJudgment();
        assertNotNull(ccJudgment);
    }
    
    
    @Test
    public void extractCourtCases() {
        
        sJudgment.setCaseNumber("CASE 211w/121");
        
        List<CourtCase> courtCases = judgmentExtractor.extractCourtCases(sJudgment);
        
        assertEquals(1, courtCases.size());
        assertEquals(sJudgment.getCaseNumber(), courtCases.get(0).getCaseNumber());
        
    }
    
    @Test
    public void extractCourtReporters() {
        
        List<String> courtReporters = judgmentExtractor.extractCourtReporters(sJudgment);
        
        assertEquals(0, courtReporters.size());
        
    }
    

      
    @Test
    public void extractDecision() {
        
        String decision = judgmentExtractor.extractDecision(sJudgment);
        
        assertNull(decision);
        
    }
    
    
    @Test
    public void extractPublisher() {
        
        String publisher = judgmentExtractor.extractPublisher(sJudgment);
        
        assertNull(publisher);
        
    }
    
    
    @Test
    public void extractReviser() {
        
        String reviser = judgmentExtractor.extractReviser(sJudgment);
        
        assertNull(reviser);
        
    }
    
    
    @Test
    public void extractSourceJudgmentId() {
        
        sJudgment.setSource(new Source());
        sJudgment.getSource().setSourceJudgmentId("1221212121222 ");
        
        String sourceJudgmentId = judgmentExtractor.extractSourceJudgmentId(sJudgment);
        
        assertEquals(sJudgment.getSource().getSourceJudgmentId(), sourceJudgmentId);
        
    }
    
    
    @Test
    public void extractSourceJudgmentUrl() {
        
        sJudgment.setSource(new Source());
        sJudgment.getSource().setSourceJudgmentUrl("www.www.pl");
        
        String sourceJudgmentUrl = judgmentExtractor.extractSourceJudgmentUrl(sJudgment);
        
        assertEquals(sJudgment.getSource().getSourceJudgmentUrl(), sourceJudgmentUrl);
        
    }
    
    
    @Test
    public void extractSummary() {
        
        String summary = judgmentExtractor.extractSummary(sJudgment);
        
        assertNull(summary);
        
    }

    
    @Test
    public void extractTextContent() {
        
        sJudgment.setTextContent("sdlsdklskd <sbfmd ck dkjcd kjcdkj cndjc\n fdfdf");
        
        String textContent = judgmentExtractor.extractTextContent(sJudgment);
        
        assertEquals(sJudgment.getTextContent(), textContent);
        
    }
    
    
    @Test
    public void extractJudgmentDate() {
        
        sJudgment.setJudgmentDate(new LocalDate());
        
        LocalDate judgmentDate = judgmentExtractor.extractJudgmentDate(sJudgment);
        
        assertEquals(sJudgment.getJudgmentDate(), judgmentDate);
        
    }
    
    
    @Test
    public void extractPublicationDate() {
        
        sJudgment.setSource(new Source());
        sJudgment.getSource().setPublicationDateTime(new DateTime());
        
        DateTime publicationDate = judgmentExtractor.extractPublicationDate(sJudgment);
        
        assertEquals(sJudgment.getSource().getPublicationDateTime(), publicationDate);
        
    }
    
    
    @Test
    public void extractJudges() {
        
        // given
        
        
        SourceScJudge sourceScJudge1 = new SourceScJudge();
        sourceScJudge1.setName("Jan Nowak");
        sourceScJudge1.setFunction("SSN");
        sourceScJudge1.setSpecialRoles(Lists.newArrayList(JudgeRole.PRESIDING_JUDGE.name(), JudgeRole.REPORTING_JUDGE.name()));
        
        SourceScJudge sourceScJudge2 = new SourceScJudge();
        sourceScJudge2.setName("Adam Kowalski");
        sourceScJudge2.setFunction("SSA");
        
        sJudgment.setJudges(Lists.newArrayList(sourceScJudge1, sourceScJudge2));
        
        
        // execute
        
        List<Judge> judges = judgmentExtractor.extractJudges(sJudgment);
        
        
        // assert
        
        assertEquals(sJudgment.getJudges().size(), judges.size());
        
        for (Judge judge : judges) {
            sJudgment.getJudges().contains(judge.getName());
            if (judge.getName().equals(sourceScJudge1.getName())) {
                assertThat(judge.getSpecialRoles(), Matchers.containsInAnyOrder(JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE));
                assertEquals(sourceScJudge1.getFunction(), judge.getFunction());
            } else {
                assertThat(judge.getSpecialRoles(), Matchers.containsInAnyOrder(sourceScJudge2.getSpecialRoles().toArray()));
                assertEquals(sourceScJudge2.getFunction(), judge.getFunction());
            }
        }
        
    }

    
    @Test
    public void extractJudgmentType() {
        
        // given
        
        sJudgment.setSupremeCourtJudgmentForm("wyrok siedmiu sędziów SN");
        
        String normalizedScJudgmentForm = "wyrok siedmiu";
        when(scJudgmentFormNameNormalizer.normalize(sJudgment.getSupremeCourtJudgmentForm())).thenReturn(normalizedScJudgmentForm);
        
        JudgmentType judgmentType = JudgmentType.SENTENCE;
        
        when(scJudgmentFormConverter.convertToType(normalizedScJudgmentForm)).thenReturn(judgmentType);
        
        
        // execute
        
        JudgmentType retJudgmentType = judgmentExtractor.extractJudgmentType(sJudgment);
        
        // assert
        
        assertEquals(judgmentType, retJudgmentType);
        
    }
    
    
    @Test
    public void extractLegalBases() {
        
        List<String> legalBases = judgmentExtractor.extractLegalBases(sJudgment);
        
        assertNotNull(legalBases);
        assertEquals(0, legalBases.size());
        
    }
    
    
    @Test
    public void extractReferencedRegulations() {
        
        List<JudgmentReferencedRegulation> referencedRegulations = judgmentExtractor.extractReferencedRegulations(sJudgment);
        
        assertNotNull(referencedRegulations);
        assertEquals(0, referencedRegulations.size());
    }


    @Test
    public void convertSpecific_PersonnelType() {
        
        // given
        
        sJudgment.setPersonnelType(PersonnelType.THREE_PERSON.name());
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        
        // execute
        
        judgmentExtractor.convertSpecific(scJudgment, sJudgment);
        
        
        // assert
        assertEquals(PersonnelType.THREE_PERSON, scJudgment.getPersonnelType());
        
        
    }
        
   
    @Test
    public void convertSpecific_SupremeCourtChambers() {
        
        
        // given 
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        
        sJudgment.setSupremeCourtChambers(Lists.newArrayList("BLEBLE CHAMBER", "Hohoho Chamber"));
        
        List<String> normalizedChamberNames = Lists.newArrayList("BLE CHAMBER", "HOH CHAMBER");
        
        when(scChamberNameNormalizer.normalize(sJudgment.getSupremeCourtChambers().get(0))).thenReturn(normalizedChamberNames.get(0));
        when(scChamberNameNormalizer.normalize(sJudgment.getSupremeCourtChambers().get(1))).thenReturn(normalizedChamberNames.get(1));
        
        SupremeCourtChamber scChamber0 = new SupremeCourtChamber();
        scChamber0.setName(normalizedChamberNames.get(0));
        
        SupremeCourtChamber scChamber1 = new SupremeCourtChamber();
        scChamber1.setName(normalizedChamberNames.get(1));
        
        when(scChamberCreator.getOrCreateScChamber(normalizedChamberNames.get(0))).thenReturn(scChamber0);
        when(scChamberCreator.getOrCreateScChamber(normalizedChamberNames.get(1))).thenReturn(scChamber1);
        
        // execute
        
        judgmentExtractor.convertSpecific(scJudgment, sJudgment);
        
        
        // assert

        assertThat(scJudgment.getScChambers(), Matchers.containsInAnyOrder(scChamber0, scChamber1));
    }



    @Test
    public void convertSpecific_SuperemeCourtChamberDivision() {
        
        // given
        
        sJudgment.setSupremeCourtChamberDivision("Izba X Wydzial 1");
        
        SupremeCourtChamberDivision scDivision = new SupremeCourtChamberDivision();
        when(scChamberDivisionCreator.getOrCreateScChamberDivision(sJudgment.getSupremeCourtChamberDivision())).thenReturn(scDivision);
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        
        
        // execute
        
        judgmentExtractor.convertSpecific(scJudgment, sJudgment);
        
        
        // assert
        assertTrue(scJudgment.getScChamberDivision() == scDivision);
        
        
    }
       
    
    @Test
    public void convertSpecific_SuperemeCourtChamberDivision_Blank() {
        
        // given
        
        sJudgment.setSupremeCourtChamberDivision(" ");
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        
        // execute
        
        judgmentExtractor.convertSpecific(scJudgment, sJudgment);
        
        
        // assert
        assertNull(scJudgment.getScChamberDivision());
        
        
    }

    
    @Test
    public void convertSpecific_SupremeCourtJudgmentForm() {
        
        // given
        
        sJudgment.setSupremeCourtJudgmentForm("wyrok siedmiu sedziow sn");
        
        String normalizedJudgmentForm = "wyrok ssn";
        when(scJudgmentFormNameNormalizer.normalize(sJudgment.getSupremeCourtJudgmentForm())).thenReturn(normalizedJudgmentForm);
        
        SupremeCourtJudgmentForm scjForm = new SupremeCourtJudgmentForm();
        scjForm.setName(normalizedJudgmentForm);
        
        when(scJudgmentFormCreator.getOrCreateScJudgmentForm(normalizedJudgmentForm)).thenReturn(scjForm);
        
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        
        
        // execute
        
        judgmentExtractor.convertSpecific(scJudgment, sJudgment);
        
        
        // assert
        assertTrue(scJudgment.getScJudgmentForm() == scjForm);
        
        
    }
    
    
    
    
    
 
}
