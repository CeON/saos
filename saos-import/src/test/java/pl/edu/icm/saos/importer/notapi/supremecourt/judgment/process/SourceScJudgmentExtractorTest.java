package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createUpdate;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.NAME;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.importer.common.correction.ImportCorrection;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
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
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    
      
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

  
    //------------------------ LOGIC --------------------------
    
    @Test
    public void createNewJudgment() {
        SupremeCourtJudgment ccJudgment = judgmentExtractor.createNewJudgment();
        assertNotNull(ccJudgment);
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
        
        // given
        
        String judgmentFormName = "wyrok siedmiu sędziów SN";
        sJudgment.setSupremeCourtJudgmentForm(judgmentFormName);
        
        JudgmentType judgmentType = JudgmentType.SENTENCE;
        
        when(scJudgmentFormConverter.convertToJudgmentType(judgmentFormName, correctionList)).thenReturn(judgmentType);
        
        
        // execute
        
        JudgmentType retJudgmentType = judgmentExtractor.extractJudgmentType(sJudgment, correctionList);
        
        // assert
        
        assertEquals(judgmentType, retJudgmentType);
        
    }


    @Test
    public void convertSpecific_PersonnelType() {
        
        // given
        
        sJudgment.setPersonnelType(PersonnelType.THREE_PERSON.name());
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        
        // execute
        
        judgmentExtractor.convertSpecific(scJudgment, sJudgment, correctionList);
        
        
        // assert
        assertEquals(PersonnelType.THREE_PERSON, scJudgment.getPersonnelType());
        
        
    }
        
   
    @Test
    public void convertSpecific_SupremeCourtChambers_OneChamberCorrected() {
        
        
        // given 
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        
        String scChamberName0 = "BLEBLE CHAMBER";
        String scChamberName1 = "HOHOhoho Chamber ";
        sJudgment.setSupremeCourtChambers(Lists.newArrayList(scChamberName0, scChamberName1));
        
        String normalizedScChamberName0 = "B L E Chamber";
        String normalizedScChamberName1 = scChamberName1;
        
        when(scChamberNameNormalizer.normalize(sJudgment.getSupremeCourtChambers().get(0))).thenReturn(normalizedScChamberName0);
        when(scChamberNameNormalizer.isChangedByNormalization(sJudgment.getSupremeCourtChambers().get(0))).thenReturn(true);
        
        when(scChamberNameNormalizer.normalize(sJudgment.getSupremeCourtChambers().get(1))).thenReturn(normalizedScChamberName1);
        when(scChamberNameNormalizer.isChangedByNormalization(sJudgment.getSupremeCourtChambers().get(1))).thenReturn(false);
        
        SupremeCourtChamber scChamber0 = new SupremeCourtChamber();
        scChamber0.setName(normalizedScChamberName0);
        
        SupremeCourtChamber scChamber1 = new SupremeCourtChamber();
        scChamber1.setName(normalizedScChamberName1);
        
        when(scChamberCreator.getOrCreateScChamber(normalizedScChamberName0)).thenReturn(scChamber0);
        when(scChamberCreator.getOrCreateScChamber(normalizedScChamberName1)).thenReturn(scChamber1);
        
        // execute
        
        judgmentExtractor.convertSpecific(scJudgment, sJudgment, correctionList);
        
        
        // assert

        assertThat(scJudgment.getScChambers(), Matchers.containsInAnyOrder(scChamber0, scChamber1));
        
        assertEquals(1, correctionList.getNumberOfCorrections());
        ImportCorrection expectedCorrection = createUpdate(scChamber0).ofProperty(NAME).oldValue(scChamberName0).newValue(normalizedScChamberName0).build();
        assertEquals(expectedCorrection, correctionList.getImportCorrections().get(0));
        
    }



    @Test
    public void convertSpecific_SupremeCourtChamberDivision() {
        
        // given
        
        sJudgment.setSupremeCourtChamberDivision("Izba X Wydzial 1");
        
        SupremeCourtChamberDivision scDivision = new SupremeCourtChamberDivision();
        when(scChamberDivisionCreator.getOrCreateScChamberDivision(sJudgment.getSupremeCourtChamberDivision())).thenReturn(scDivision);
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        
        
        // execute
        
        judgmentExtractor.convertSpecific(scJudgment, sJudgment, correctionList);
        
        
        // assert
        assertTrue(scJudgment.getScChamberDivision() == scDivision);
        
        
    }
       
    
    @Test
    public void convertSpecific_SupremeCourtChamberDivision_Blank() {
        
        // given
        
        sJudgment.setSupremeCourtChamberDivision(" ");
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        
        // execute
        
        judgmentExtractor.convertSpecific(scJudgment, sJudgment, correctionList);
        
        
        // assert
        assertNull(scJudgment.getScChamberDivision());
        
        
    }

    
    @Test
    public void convertSpecific_SupremeCourtJudgmentForm_NoCorrection() {
        
        // given
        
        String judgmentFormName = "wyrok siedmiu sedziow sn";
        sJudgment.setSupremeCourtJudgmentForm(judgmentFormName);
        
        when(scJudgmentFormNameNormalizer.normalize(sJudgment.getSupremeCourtJudgmentForm())).thenReturn(judgmentFormName);
        when(scJudgmentFormNameNormalizer.isChangedByNormalization(judgmentFormName)).thenReturn(false);
        
        SupremeCourtJudgmentForm scjForm = new SupremeCourtJudgmentForm();
        scjForm.setName(judgmentFormName);
        
        when(scJudgmentFormCreator.getOrCreateScJudgmentForm(judgmentFormName)).thenReturn(scjForm);
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        
        
        // execute
        
        judgmentExtractor.convertSpecific(scJudgment, sJudgment, correctionList);
        
        
        // assert
        assertTrue(scJudgment.getScJudgmentForm() == scjForm);
        assertEquals(0, correctionList.getNumberOfCorrections());
        
    }
    
    
    @Test
    public void convertSpecific_SupremeCourtJudgmentForm_WithCorrection() {
        
        // given
        
        String judgmentFormName = "wyrok siedmiu sedziow sn";
        sJudgment.setSupremeCourtJudgmentForm(judgmentFormName);
        
        String normalizedJudgmentFormName = "wyrok 7 wspaniałych";
        when(scJudgmentFormNameNormalizer.normalize(sJudgment.getSupremeCourtJudgmentForm())).thenReturn(normalizedJudgmentFormName);
        when(scJudgmentFormNameNormalizer.isChangedByNormalization(judgmentFormName)).thenReturn(true);
        
        SupremeCourtJudgmentForm scjForm = new SupremeCourtJudgmentForm();
        scjForm.setName(normalizedJudgmentFormName);
        
        when(scJudgmentFormCreator.getOrCreateScJudgmentForm(normalizedJudgmentFormName)).thenReturn(scjForm);
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        
        
        // execute
        
        judgmentExtractor.convertSpecific(scJudgment, sJudgment, correctionList);
        
        
        // assert
        assertTrue(scJudgment.getScJudgmentForm() == scjForm);
        assertEquals(1, correctionList.getNumberOfCorrections());

        ImportCorrection expectedCorrection = createUpdate(scjForm).ofProperty(NAME).oldValue(judgmentFormName).newValue(normalizedJudgmentFormName).build();
        assertEquals(expectedCorrection, correctionList.getImportCorrections().get(0));

    }
    
    
    
    
    
 
}
