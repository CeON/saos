package pl.edu.icm.saos.persistence.common;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;
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
        saveCtJudgment(testObjectContext.getCtJudgment());
        saveNacJudgment(testObjectContext.getNacJudgment());

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
     * Creates {@link ConstitutionalTribunalJudgment} hierarchy with default field data.
     * @return ConstitutionalTribunalJudgment.
     */
    @Transactional
    public ConstitutionalTribunalJudgment createCtJudgment(){
        ConstitutionalTribunalJudgment ctJudgment = TestInMemoryCtObjectFactory.createCtJudgment();
        saveCtJudgment(ctJudgment);
        return ctJudgment;
    }
    
    /**
     * Creates {@link NationalAppealChamberJudgment} hierarchy with default field data.
     * @return NationalAppealChamberJudgment.
     */
    @Transactional
    public NationalAppealChamberJudgment createNacJudgment(){
        NationalAppealChamberJudgment nacJudgment = TestInMemoryNacObjectFactory.createNacJudgment();
        saveNacJudgment(nacJudgment);
        return nacJudgment;
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
    public CommonCourt createCcCourt(CommonCourtType courtType){
        CommonCourt ccCourt = TestInMemoryObjectFactory.createCcCourt(courtType);
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
        CommonCourtJudgment ccJudgment = TestInMemoryObjectFactory.createSimpleCcJudgment();
        saveCcJudgment(ccJudgment);
        return ccJudgment;
    }

    /**
     * Creates {@link ConstitutionalTribunalJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return ConstitutionalTribunalJudgment
     */
    @Transactional
    public ConstitutionalTribunalJudgment createSimpleCtJudgment(){
        ConstitutionalTribunalJudgment ctJudgment = TestInMemoryCtObjectFactory.createSimpleCtJudgment();
        saveCtJudgment(ctJudgment);
        return ctJudgment;
    }
    
    /**
     * Creates {@link NationalAppealChamberJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return NationalAppealChamberJudgment
     */
    @Transactional
    public NationalAppealChamberJudgment createSimpleNacJudgment(){
        NationalAppealChamberJudgment nacJudgment = TestInMemoryNacObjectFactory.createSimpleNacJudgment();
        saveNacJudgment(nacJudgment);
        return nacJudgment;
    }

    /**
     * Creates list of {@link SupremeCourtJudgment} with fields filled with random values.
     * @param size of the list.
     * @return SupremeCourtJudgment
     */
    @Transactional
    public List<SupremeCourtJudgment> createScJudgmentListWithRandomData(int size){
        List<SupremeCourtJudgment> judgments = TestInMemoryObjectFactory.createScJudgmentListWithRandomData(size);
        judgments.forEach(scJudgment -> saveScJudgment(scJudgment));
        
        return judgments;
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

    /**
     * Creates list of {@link ConstitutionalTribunalJudgment} with fields filled with random values.
     * @param size of the list.
     * @return list of ConstitutionalTribunalJudgment
     */
    @Transactional
    public List<ConstitutionalTribunalJudgment> createCtJudgmentListWithRandomData(int size){
        List<ConstitutionalTribunalJudgment> judgments = TestInMemoryCtObjectFactory.createCtJudgmentListWithRandomData(size);
        judgments.forEach(ctJudgment -> saveCtJudgment(ctJudgment));

        return judgments;
    }
    
    /**
     * Creates list of {@link NationalAppealChamberJudgment} with fields filled with random values.
     * @param size of the list.
     * @return list of NationalAppealChamberJudgment
     */
    @Transactional
    public List<NationalAppealChamberJudgment> createNacJudgmentListWithRandomData(int size){
        List<NationalAppealChamberJudgment> judgments = TestInMemoryNacObjectFactory.createNacJudgmentListWithRandomData(size);
        judgments.forEach(ctJudgment -> saveNacJudgment(ctJudgment));

        return judgments;
    }
    
    /**
     * Creates list of {@link EnrichmentTag} with default field data for judgment with provided id.
     * @param judgmentId
     * @return list of EnrichmentTag
     */
    @Transactional
    public List<EnrichmentTag> createEnrichmentTagsForJudgment(long judgmentId) {
        List<EnrichmentTag> enrichmentTags = TestInMemoryEnrichmentTagFactory.createEnrichmentTagsForJudgment(judgmentId);
        enrichmentTags.forEach(enrichmentTag -> saveEnrichmentTag(enrichmentTag));
        
        return enrichmentTags;
    }
    
    /**
     * Creates {@link EnrichmentTag} with
     * {@link pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes#REFERENCED_COURT_CASES} type
     * referencing judgments provided as argument
     * 
     * @param judgmentId
     * @param referencedJudgments
     * @return
     */
    @Transactional
    public EnrichmentTag createReferencedCourtCasesTag(long judgmentId, Judgment ... referencedJudgments) {
        EnrichmentTag enrichmentTag = TestInMemoryEnrichmentTagFactory.createReferencedCourtCasesTag(judgmentId, referencedJudgments);
        saveEnrichmentTag(enrichmentTag);
        
        return enrichmentTag;
    }
    
    /**
     * Creates {@link EnrichmentTag} with
     * {@link pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes#REFERENCED_REGULATIONS} type
     */
    @Transactional
    public EnrichmentTag createReferencedRegulationsTag(long judgmentId, String refRegulationTextPrefix, LawJournalEntry ... lawJournalEntries) {
        EnrichmentTag enrichmentTag = TestInMemoryEnrichmentTagFactory.createReferencedRegulationsTag(judgmentId, refRegulationTextPrefix, lawJournalEntries);
        saveEnrichmentTag(enrichmentTag);
        
        return enrichmentTag;
    }


    //------------------------ PRIVATE --------------------------
    @Transactional
    private void saveCcJudgment(CommonCourtJudgment ccJudgment){
        CommonCourtDivision division = ccJudgment.getCourtDivision();
        if(division!=null){
            CommonCourt ccCourt = division.getCourt();
            saveCcCourt(ccCourt, false);
        }
        
        saveJudgmentCommonAttributes(ccJudgment);

        entityManager.persist(ccJudgment);

        entityManager.flush();
    }

    @Transactional
    private void saveCcCourt(CommonCourt ccCourt, boolean flush){
        
        
        for (CommonCourtDivision ccDivision : ccCourt.getDivisions()) {
            entityManager.persist(ccDivision.getType());
        }

        
        entityManager.persist(ccCourt);

        CommonCourt parentCourt = ccCourt.getParentCourt();
        if(parentCourt != null){
            entityManager.persist(parentCourt);
        
            if(parentCourt.getParentCourt() != null){
                entityManager.persist(parentCourt.getParentCourt());
            }
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


        saveJudgmentCommonAttributes(scJudgment);

        if(scJudgment.getScJudgmentForm() != null){
            entityManager.persist(scJudgment.getScJudgmentForm());
        }

        entityManager.persist(scJudgment);

        entityManager.flush();
    }

    @Transactional
    private void saveCtJudgment(ConstitutionalTribunalJudgment ctJudgment){
        
        saveJudgmentCommonAttributes(ctJudgment);

        entityManager.persist(ctJudgment);

        entityManager.flush();
    }
    
    @Transactional
    private void saveNacJudgment(NationalAppealChamberJudgment nacJudgment){
        
        saveJudgmentCommonAttributes(nacJudgment);

        entityManager.persist(nacJudgment);

        entityManager.flush();
    }

    @Transactional
    private void saveScChamber(SupremeCourtChamber scChamber, boolean flush){
        entityManager.persist(scChamber);

        if(flush){
            entityManager.flush();
        }
    }
    
    @Transactional
    private void saveJudgmentCommonAttributes(Judgment judgment) {
        
        if (judgment.getJudgmentResult() != null) {
            entityManager.persist(judgment.getJudgmentResult());
        }
        if (judgment.getMeansOfAppeal() != null) {
            entityManager.persist(judgment.getMeansOfAppeal());
        }
        
        for(JudgmentReferencedRegulation referencedRegulation: judgment.getReferencedRegulations()){
            entityManager.persist(referencedRegulation.getLawJournalEntry());
        }
        
    }
    
    @Transactional
    private void saveEnrichmentTag(EnrichmentTag enrichmentTag) {
        entityManager.persist(enrichmentTag);
        entityManager.flush();
    }


}
