package pl.edu.icm.saos.persistence.common;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.*;

import java.util.List;

/**
 * Provides factory methods for model object creation.
 * Created objects aren't saved in db.
 * So it can be used in unit test.
 * See also {@link TestPersistenceObjectFactory}
 * @author pavtel
 */
public final class TestInMemoryObjectFactory {
    private TestInMemoryObjectFactory() {
    }

    //------------------------ LOGIC --------------------------
    /**
     * Creates {@link TestObjectContext} hierarchy with default field data.
     * @return TestObjectContext.
     */
    public static TestObjectContext createTestObjectContext(){
        CommonCourtJudgment ccJudgment = TestInMemoryCcObjectFactory.createCcJudgment();
        SupremeCourtJudgment scJudgment = TestInMemoryScObjectFactory.createScJudgment();
        ConstitutionalTribunalJudgment ctJudgment = TestInMemoryCtObjectFactory.createCtJudgment();
        NationalAppealChamberJudgment nacJudgment = TestInMemoryNacObjectFactory.createNacJudgment();

        TestObjectContext testObjectContext = new TestObjectContext();
        testObjectContext.setCcJudgment(ccJudgment);
        testObjectContext.setScJudgment(scJudgment);
        testObjectContext.setCtJudgment(ctJudgment);
        testObjectContext.setNacJudgment(nacJudgment);

        return testObjectContext;
    }

    /**
     * Creates {@link CommonCourtJudgment} hierarchy with default field data.
     * @return CommonCourtJudgment.
     */
    public static CommonCourtJudgment createCcJudgment(){
        return TestInMemoryCcObjectFactory.createCcJudgment();
    }

    /**
     * Creates {@link SupremeCourtJudgment} hierarchy with default field data.
     * @return SupremeCourtJudgment.
     */
    public static SupremeCourtJudgment createScJudgment(){
        return TestInMemoryScObjectFactory.createScJudgment();
    }

    /**
     * Creates {@link pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment} hierarchy
     * with default field data.
     * @return ConstitutionalTribunalJudgment.
     */
    public static ConstitutionalTribunalJudgment createCtJudgment(){
        return TestInMemoryCtObjectFactory.createCtJudgment();
    }
    
    /**
     * Creates {@link pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment} hierarchy
     * with default field data.
     * @return NationalAppealChamberJudgment.
     */
    public static NationalAppealChamberJudgment createNacJudgment(){
        return TestInMemoryNacObjectFactory.createNacJudgment();
    }

    /**
     * Creates {@link pl.edu.icm.saos.persistence.model.SupremeCourtChamber} hierarchy with default field data.
     * @return SupremeCourtJudgment.
     */
    public static SupremeCourtChamber createScChamber(){
        return TestInMemoryScObjectFactory.createScChamber();
    }


    /**
     * Creates {@link pl.edu.icm.saos.persistence.model.CommonCourt} hierarchy with default field data.
     * @return CommonCourt
     */
    public static CommonCourt createCcCourt(boolean withParent){
        return TestInMemoryCcObjectFactory.createCcCourt(withParent);
    }

    /**
     * Creates list of {@link CommonCourt} with fields filled with random values.
     * @param size of the list.
     * @return list of CommonCourt
     */
    public static List<CommonCourt> createCcCourtListWithRandomData(int size){
        return TestInMemoryCcObjectFactory.createCcCourtListWithRandomData(size);
    }


    /**
     * Creates {@link SupremeCourtJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return SupremeCourtJudgment
     */
    public static SupremeCourtJudgment createSimpleScJudgment(){
        return TestInMemoryScObjectFactory.createSimpleScJudgment();
    }

    /**
     * Creates {@link CommonCourtJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return CommonCourtJudgment
     */
    public static CommonCourtJudgment createSimpleCcJudgment(){
        return TestInMemoryCcObjectFactory.createSimpleCcJudgment();
    }

    /**
     * Creates {@link ConstitutionalTribunalJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return ConstitutionalTribunalJudgment
     */
    public static ConstitutionalTribunalJudgment createSimpleCtJudgment(){
        return TestInMemoryCtObjectFactory.createSimpleCtJudgment();
    }
    
    /**
     * Creates {@link NationalAppealChamberJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return NationalAppealChamberJudgment
     */
    public static NationalAppealChamberJudgment createSimpleNacJudgment(){
        return TestInMemoryNacObjectFactory.createSimpleNacJudgment();
    }

    /**
     * Creates list of {@link SupremeCourtJudgment} with fields filled with random values.
     * @param size of the list.
     * @return list of SupremeCourtJudgment
     */
    public static List<SupremeCourtJudgment> createScJudgmentListWithRandomData(int size){
        return TestInMemoryScObjectFactory.createScJudgmentListWithRandomData(size);
    }
    
    /**
     * Creates list of {@link CommonCourtJudgment} with fields filled with random values.
     * @param size of the list.
     * @return list of CommonCourtJudgment
     */
    public static List<CommonCourtJudgment> createCcJudgmentListWithRandomData(int size){
        return TestInMemoryCcObjectFactory.createCcJudgmentListWithRandomData(size);
    }

    /**
     * Creates list of {@link ConstitutionalTribunalJudgment} with fields filled with random values.
     * @param size of the list.
     * @return list of ConstitutionalTribunalJudgment
     */
    public static List<ConstitutionalTribunalJudgment> createCtJudgmentListWithRandomData(int size){
        return TestInMemoryCtObjectFactory.createCtJudgmentListWithRandomData(size);
    }

    /**
     * Creates list of {@link NationalAppealChamberJudgment} with fields filled with random values.
     * @param size of the list.
     * @return list of NationalAppealChamberJudgment
     */
    public static List<NationalAppealChamberJudgment> createNacJudgmentListWithRandomData(int size){
        return TestInMemoryNacObjectFactory.createNacJudgmentListWithRandomData(size);
    }
    
    /**
     * Creates list of {@link EnrichmentTag} with default field data for judgment with provided id.
     * @param judgmentId
     * @return list of EnrichmentTag
     */
    public static List<EnrichmentTag> createEnrichmentTagsForJudgment(int judgmentId) {
        return TestInMemoryEnrichmentTagFactory.createEnrichmentTagsForJudgment(judgmentId);
    }



}
