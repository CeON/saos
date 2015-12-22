package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.importer.ImportTestSupport;
import pl.edu.icm.saos.importer.notapi.common.RawJudgmentTestFactory;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
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
    private ScChamberRepository scChamberRepository;
    
    @Autowired
    private ScChamberDivisionRepository scChamberDivisionRepository;
    
    @Autowired
    private ScJudgmentFormRepository scJudgmentFormRepository;

    @Autowired
    private RawJudgmentTestFactory rawJudgmentTestFactory;
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    @Test
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
    public void deleteScChamberDivisionsWithoutJudgments() {
        
        // given 
        
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        scChamber.setName(RandomStringUtils.randomAlphanumeric(12));
        scChamberRepository.save(scChamber);
        
        SupremeCourtChamberDivision scDivision1 = createScChamberDivision(scChamber, true);
        
        createScChamberDivision(scChamber, false);
        createScChamberDivision(scChamber, false);
        
        
        //execute
        
        scObjectDeleter.deleteScChamberDivisionsWithoutJudgments();
        
        
        // assert
        
        assertEquals(1, scChamberDivisionRepository.count());
        List<SupremeCourtChamberDivision> dbScChamberDivisions = scChamberDivisionRepository.findAll();
        
        assertEquals(1, dbScChamberDivisions.size());
        assertEquals(scDivision1.getId(), dbScChamberDivisions.get(0).getId());
        
    }
    
    
    
    @Test
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
    
    
    private SupremeCourtChamber createScChamber(boolean hasReferringJudgment) {
        
        
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        scChamber.setName(RandomStringUtils.randomAlphanumeric(12));
        
        scChamberRepository.save(scChamber);
        scChamberRepository.flush();
        
        createScChamberDivision(scChamber, hasReferringJudgment);
        
        return scChamber;
        
    }


    private SupremeCourtChamberDivision createScChamberDivision(SupremeCourtChamber scChamber, boolean hasReferringJudgment) {
        
        SupremeCourtChamberDivision scChamberDivision = new SupremeCourtChamberDivision();
        scChamberDivision.setName(RandomStringUtils.randomAlphanumeric(8));
        scChamberDivision.setFullName(RandomStringUtils.randomAlphanumeric(15));
        
        scChamber.addDivision(scChamberDivision);
        
        scChamberDivisionRepository.save(scChamberDivision);
        scChamberDivisionRepository.flush();
        
        scChamber = scChamberRepository.save(scChamber);
        scChamberRepository.flush();
        
        
        if (hasReferringJudgment) {
            SupremeCourtJudgment judgment = rawJudgmentTestFactory.createScJudgment(false);
            judgment.setScChamberDivision(scChamberDivision);
            judgment.addScChamber(scChamber);
            judgmentRepository.save(judgment);  
        }

        
        
        return scChamberDivision;
    }
    
    
  private SupremeCourtJudgmentForm createScjForm(boolean hasReferringJudgment) {
        
        
      SupremeCourtJudgmentForm scjForm = new SupremeCourtJudgmentForm();
      scjForm.setName(RandomStringUtils.randomAlphanumeric(12));;
      
      scJudgmentFormRepository.save(scjForm);  
       
      if (hasReferringJudgment) {
            SupremeCourtJudgment judgment = rawJudgmentTestFactory.createScJudgment(false);
            judgment.setScJudgmentForm(scjForm);
            judgmentRepository.save(judgment);    
      }
        
      return scjForm;
        
    }
    
}
