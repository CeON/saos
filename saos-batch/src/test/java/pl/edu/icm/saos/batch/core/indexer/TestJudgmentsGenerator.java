package pl.edu.icm.saos.batch.core.indexer;

import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_CHAMBER_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_FIRST_DIVISION_FULL_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_FIRST_DIVISION_NAME;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.common.TextObjectDefaultData;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
@Service("testJudgmentsGenerator")
public class TestJudgmentsGenerator {

    @Autowired
    private JudgmentRepository judgmentRepository; 
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    @Autowired
    private EntityManager entityManager;
    
    
    //------------------------ LOGIC --------------------------
    
    public List<CommonCourtJudgment> generateCcJudgments(int count) {
        CommonCourt commonCourt = testPersistenceObjectFactory.createCcCourt(CommonCourtType.DISTRICT);
        
        CommonCourt[] commonCourts = new CommonCourt[] {commonCourt, commonCourt.getRegionalCourt(), commonCourt.getAppealCourt()};
        
        List<CommonCourtJudgment> ccJudgments = testPersistenceObjectFactory.createCcJudgmentListWithRandomData(count);
        
        int i = 0;
        for (CommonCourtJudgment ccJudgment : ccJudgments) {
            ccJudgment.setCourtDivision(commonCourts[i % 2].getDivisions().get(0));
            i++;
        }
        
        judgmentRepository.save(ccJudgments);
        judgmentRepository.flush();
        
        return ccJudgments;
    }
    
    public List<SupremeCourtJudgment> generateScJudgments(int count) {
        SupremeCourtChamber supremeCourtChamber = testPersistenceObjectFactory.createScChamber();
        
        List<SupremeCourtJudgment> scJudgments = testPersistenceObjectFactory
                .createScJudgmentListWithRandomData(count);
        
        scJudgments.forEach(x -> x.addScChamber(supremeCourtChamber));
        scJudgments.forEach(x -> x.setScChamberDivision(x.getScChambers().get(0).getDivisions().get(0)));
        judgmentRepository.save(scJudgments);
        judgmentRepository.flush();
        
        return scJudgments;
    }
    
    public List<ConstitutionalTribunalJudgment> generateCtJudgments(int count) {
        return testPersistenceObjectFactory.createCtJudgmentListWithRandomData(count);
        
        
    }
    
    public List<NationalAppealChamberJudgment> generateNacJudgments(int count) {
        return testPersistenceObjectFactory.createNacJudgmentListWithRandomData(count);
    }
    
    @Transactional
    public CommonCourtDivision createCcDivision(String postfix) {
        CommonCourt commonCourt = new CommonCourt();
        
        commonCourt.setCode(TextObjectDefaultData.CC_COURT_CODE + postfix);
        commonCourt.setName(TextObjectDefaultData.CC_COURT_NAME + postfix);
        commonCourt.setType(CommonCourtType.APPEAL);
        
        CommonCourtDivision ccDivision = new CommonCourtDivision();
        ccDivision.setCode(TextObjectDefaultData.CC_DIVISION_CODE + postfix);
        ccDivision.setName(TextObjectDefaultData.CC_DIVISION_NAME + postfix);
        ccDivision.setCourt(commonCourt);
        
        entityManager.persist(commonCourt);
        entityManager.persist(ccDivision);
        
        return ccDivision;
    }
    
    @Transactional
    public SupremeCourtJudgmentForm createScJudgmentForm(String postfix) {
        SupremeCourtJudgmentForm newJudgmentForm = new SupremeCourtJudgmentForm();
        newJudgmentForm.setName(TextObjectDefaultData.SC_JUDGMENT_FORM_NAME + postfix);
        entityManager.persist(newJudgmentForm);
        return newJudgmentForm;
    }
    
    @Transactional
    public SupremeCourtChamber createScCourtChamber(String postfix) {
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        scChamber.setName(SC_CHAMBER_NAME + postfix);

        SupremeCourtChamberDivision scDivision = new SupremeCourtChamberDivision();
        scDivision.setName(SC_FIRST_DIVISION_NAME + postfix);
        scDivision.setFullName(SC_FIRST_DIVISION_FULL_NAME + postfix);
        scChamber.addDivision(scDivision);
        
        entityManager.persist(scChamber);
        entityManager.persist(scDivision);
        
        
        return scChamber;
    }
    
}
