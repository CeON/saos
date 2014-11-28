package pl.edu.icm.saos.persistence.common;

import pl.edu.icm.saos.persistence.model.*;

import java.util.List;

/**
 * Provides factory methods for model object creation.
 * Created objects aren't saved in db.
 * So it can be used in unit test.
 * See also {@link TestPersistenceObjectFactory}
 * @author pavtel
 */
public abstract class TestInMemoryObjectFactory {


    //------------------------ LOGIC --------------------------
    /**
     * Creates {@link TestObjectContext} hierarchy with default field data.
     * @return TestObjectContext.
     */
    public static TestObjectContext createTestObjectContext(){
        CommonCourtJudgment ccJudgment = TestInMemoryCcObjectFactory.createCcJudgment();
        SupremeCourtJudgment scJudgment = TestInMemoryScObjectFactory.createScJudgment();

        TestObjectContext testObjectContext = new TestObjectContext();
        testObjectContext.setCcJudgment(ccJudgment);
        testObjectContext.setScJudgment(scJudgment);

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
    public static CommonCourt createCcCourt(boolean save){
        return TestInMemoryCcObjectFactory.createCcCourt();
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
     * Creates list of {@link CommonCourtJudgment} with fields filled with random values.
     * @param size of the list.
     * @return CommonCourtJudgment
     */
    public static List<CommonCourtJudgment> createCcJudgmentListWithRandomData(int size){
        return TestInMemoryCcObjectFactory.createCcJudgmentListWithRandomData(size);
    }

    /**
     * Creates list of {@link pl.edu.icm.saos.persistence.model.CcJudgmentKeyword} with random data and given size.
     * @param size of the list.
     * @return keywords list.
     */
    public static List<CcJudgmentKeyword> createCcKeywordListWith(int size){
        return TestInMemoryCcObjectFactory.createCcKeywordListWith(size);
    }


}
