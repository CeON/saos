package pl.edu.icm.saos.persistence.common;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * Provides factory methods for model object creation.
 * Created objects are saved in db.
 * So it can be used in integration test.
 * See also {@link TestInMemoryObjectFactory}
 * @author pavtel
 */
@Service
public class TestPersistenceObjectFactory {

    @Autowired
    private EntityManager entityManager;



    //------------------------ LOGIC --------------------------
    /**
     * Creates {@link TestObjectContext} hierarchy with default field data.
     * @return TestObjectContext.
     */
    @Transactional
    public TestObjectContext createTestObjectContext(){
        TestObjectContext testObjectContext = TestInMemoryObjectFactory.createTestObjectContext();

        saveCcJudgment(testObjectContext.getCcJudgment());
        saveScJudgment(testObjectContext.getScJudgment());

        return testObjectContext;
    }

    /**
     * Creates {@link CommonCourtJudgment} hierarchy with default field data.
     * @return CommonCourtJudgment.
     */
    @Transactional
    public CommonCourtJudgment createCcJudgment(){
        CommonCourtJudgment ccJudgment = TestInMemoryObjectFactory.createCcJudgment();
        saveCcJudgment(ccJudgment);
        return ccJudgment;
    }

    /**
     * Creates {@link SupremeCourtJudgment} hierarchy with default field data.
     * @return SupremeCourtJudgment.
     */
    @Transactional
    public SupremeCourtJudgment createScJudgment(){
        SupremeCourtJudgment scJudgment = TestInMemoryObjectFactory.createScJudgment();
        saveScJudgment(scJudgment);
        return scJudgment;
    }

    /**
     * Creates {@link pl.edu.icm.saos.persistence.model.SupremeCourtChamber} hierarchy with default field data.
     * @return SupremeCourtJudgment.
     */
    @Transactional
    public SupremeCourtChamber createScChamber(){
        SupremeCourtChamber scChamber = TestInMemoryObjectFactory.createScChamber();
        saveScChamber(scChamber, true);
        return scChamber;
    }


    /**
     * Creates {@link pl.edu.icm.saos.persistence.model.CommonCourt} hierarchy with default field data.
     * @return CommonCourt
     */
    @Transactional
    public CommonCourt createCcCourt(boolean withParent){
        CommonCourt ccCourt = TestInMemoryObjectFactory.createCcCourt(withParent);
        saveCcCourt(ccCourt, true);
        return ccCourt;
    }

    /**
     * Creates list of {@link CommonCourt} with fields filled with random values.
     * @param size of the list.
     * @return list of CommonCourt
     */
    @Transactional
    public List<CommonCourt> createCcCourtListWithRandomData(int size){
        List<CommonCourt> commonCourts = TestInMemoryCcObjectFactory.createCcCourtListWithRandomData(size);
        commonCourts.forEach(ccCourt -> saveCcCourt(ccCourt, true));
        return commonCourts;
    }


    /**
     * Creates {@link SupremeCourtJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return SupremeCourtJudgment
     */
    @Transactional
    public SupremeCourtJudgment createSimpleScJudgment(){
        SupremeCourtJudgment scJudgment = TestInMemoryObjectFactory.createSimpleScJudgment();
        saveScJudgment(scJudgment);
        return scJudgment;
    }

    /**
     * Creates {@link CommonCourtJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return CommonCourtJudgment
     */
    @Transactional
    public CommonCourtJudgment createSimpleCcJudgment(){
        CommonCourtJudgment ccJudgment = TestInMemoryObjectFactory.createCcJudgment();
        saveCcJudgment(ccJudgment);
        return ccJudgment;
    }

    /**
     * Creates list of {@link CommonCourtJudgment} with fields filled with random values.
     * @param size of the list.
     * @return CommonCourtJudgment
     */
    @Transactional
    public List<CommonCourtJudgment> createCcJudgmentListWithRandomData(int size){
        List<CommonCourtJudgment> judgments = TestInMemoryObjectFactory.createCcJudgmentListWithRandomData(size);
        judgments.forEach(ccJudgment -> saveCcJudgment(ccJudgment));

        return judgments;
    }


    //------------------------ PRIVATE --------------------------
    @Transactional
    private void saveCcJudgment(CommonCourtJudgment ccJudgment){
        CommonCourtDivision division = ccJudgment.getCourtDivision();
        if(division!=null){
            CommonCourt ccCourt = division.getCourt();
            saveCcCourt(ccCourt, false);
        }


        for(JudgmentReferencedRegulation referencedRegulation: ccJudgment.getReferencedRegulations()){
            entityManager.persist(referencedRegulation.getLawJournalEntry());
        }

        entityManager.persist(ccJudgment);

        entityManager.flush();
    }

    @Transactional
    private void saveCcCourt(CommonCourt ccCourt, boolean flush){
        entityManager.persist(ccCourt);

        CommonCourt parentCourt = ccCourt.getParentCourt();
        if(parentCourt != null){
            entityManager.persist(parentCourt);
        }

        for (CommonCourtDivision ccDivision : ccCourt.getDivisions()) {
            entityManager.persist(ccDivision.getType());
        }

        if(flush){
            entityManager.flush();
        }
    }

    @Transactional
    private void saveScJudgment(SupremeCourtJudgment scJudgment){
        SupremeCourtChamberDivision scDivision =  scJudgment.getScChamberDivision();
        if(scDivision != null){
            SupremeCourtChamber scChamber = scDivision.getScChamber();
            saveScChamber(scChamber, false);
        }


        for(JudgmentReferencedRegulation referencedRegulation: scJudgment.getReferencedRegulations()){
            entityManager.persist(referencedRegulation.getLawJournalEntry());
        }

        if(scJudgment.getScJudgmentForm() != null){
            entityManager.persist(scJudgment.getScJudgmentForm());
        }

        entityManager.persist(scJudgment);

        entityManager.flush();
    }

    @Transactional
    private void saveScChamber(SupremeCourtChamber scChamber, boolean flush){
        entityManager.persist(scChamber);

        if(flush){
            entityManager.flush();
        }
    }


}
