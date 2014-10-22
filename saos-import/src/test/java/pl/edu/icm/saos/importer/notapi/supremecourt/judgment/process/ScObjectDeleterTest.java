package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.importer.ImportTestSupport;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RawSourceScJudgmentRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class ScObjectDeleterTest extends ImportTestSupport {

    @Autowired
    private ScObjectDeleter scObjectDeleter;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private RawSourceScJudgmentRepository rawSourceScJudgmentRepository;
    
    @Autowired
    private ScChamberRepository scChamberRepository;
    
    @Autowired
    private ScChamberDivisionRepository scChamberDivisionRepository;
    
    @Autowired
    private ScJudgmentFormRepository scJudgmentFormRepository;
    
    
    
    @Test
    public void deleteJudgmentsWithoutRawSourceScJudgment() {
        
        SupremeCourtJudgment scJudgment1 = createScJudgment(true);
        createScJudgment(false);
        createScJudgment(false);
        createScJudgment(false);
        
        scObjectDeleter.deleteJudgmentsWithoutRawSourceScJudgment();
        
        List<Judgment> dbScJudgments = judgmentRepository.findAll();
        
        assertEquals(1, dbScJudgments.size());
        assertEquals(scJudgment1.getId(), dbScJudgments.get(0).getId());
        
        
    }
    
    
    @Test
    public void deleteJudgmentsWithoutRawSourceScJudgment_NoSuchJudgments() {
        
        SupremeCourtJudgment scJudgment1 = createScJudgment(true);
        SupremeCourtJudgment scJudgment2 = createScJudgment(true);
        
        scObjectDeleter.deleteJudgmentsWithoutRawSourceScJudgment();
        
        List<Judgment> dbScJudgments = judgmentRepository.findAll();
        
        assertEquals(2, dbScJudgments.size());
        assertThat(dbScJudgments, Matchers.containsInAnyOrder(scJudgment1, scJudgment2));
        
        
    }
    
    
    @Test
    @Transactional
    public void deleteScChambersWithoutJudgments() {
        
        SupremeCourtChamber scChamber = createScChamber(true);
        createScChamber(false);
        createScChamber(false);
        createScChamber(false);
        
        scObjectDeleter.deleteScChambersWithoutJudgments();
        
        List<SupremeCourtChamber> dbScChambers = scChamberRepository.findAll();
        
        assertEquals(1, dbScChambers.size());
        assertEquals(scChamber.getId(), dbScChambers.get(0).getId());
        
        
    }
    
    
    @Test
    @Transactional
    public void deleteScjFormsWithoutJudgments() {
        
        SupremeCourtJudgmentForm scjForm = createScjForm(true);
        createScjForm(false);
        createScjForm(false);
        createScjForm(false);
        
        scObjectDeleter.deleteScjFormsWithoutJudgments();
        
        List<SupremeCourtJudgmentForm> dbScjForms = scJudgmentFormRepository.findAll();
        
        assertEquals(1, dbScjForms.size());
        assertEquals(scjForm.getId(), dbScjForms.get(0).getId());
        
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    public SupremeCourtJudgment createScJudgment(boolean hasCorrespondingSourceJudgment) {
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        scJudgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.randomAlphanumeric(10));
        scJudgment.addCourtCase(new CourtCase("123"));
        
        
        if (hasCorrespondingSourceJudgment) {
            RawSourceScJudgment rJudgment = new RawSourceScJudgment();
            rJudgment.setJsonContent("dds");
            rJudgment.setSourceId(scJudgment.getSourceInfo().getSourceJudgmentId());
            rawSourceScJudgmentRepository.save(rJudgment);
        }
        
        judgmentRepository.save(scJudgment);
        
        
        
        return scJudgment;
        
    }
    
    
    private SupremeCourtChamber createScChamber(boolean hasReferringJudgment) {
        
        
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        scChamber.setName(RandomStringUtils.randomAlphanumeric(12));
        
        
        SupremeCourtChamberDivision scChamberDivision = new SupremeCourtChamberDivision();
        scChamberDivision.setName(RandomStringUtils.randomAlphanumeric(8));
        scChamberDivision.setFullName(RandomStringUtils.randomAlphanumeric(15));
        
        scChamber.addDivision(scChamberDivision);
        
        scChamberRepository.save(scChamber);
        scChamberDivisionRepository.save(scChamberDivision);
        
        if (hasReferringJudgment) {
            SupremeCourtJudgment judgment = new SupremeCourtJudgment();
            judgment.addCourtCase(new CourtCase("123"));
            judgment.setScChamberDivision(scChamberDivision);
            judgment.addScChamber(scChamber);
            judgmentRepository.save(judgment);    
        }
        
        return scChamber;
        
    }
    
    
  private SupremeCourtJudgmentForm createScjForm(boolean hasReferringJudgment) {
        
        
      SupremeCourtJudgmentForm scjForm = new SupremeCourtJudgmentForm();
      scjForm.setName(RandomStringUtils.randomAlphanumeric(12));;
      
      scJudgmentFormRepository.save(scjForm);  
       
      if (hasReferringJudgment) {
            SupremeCourtJudgment judgment = new SupremeCourtJudgment();
            judgment.addCourtCase(new CourtCase("123"));
            judgment.setScJudgmentForm(scjForm);
            judgmentRepository.save(judgment);    
      }
        
      return scjForm;
        
    }
    
}
