package pl.edu.icm.saos.persistence.common;

import java.util.List;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

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
     * Creates list of {@link CommonCourtJudgment} with fields filled with random values.
     * @param size of the list.
     * @return CommonCourtJudgment
     */
    public static List<CommonCourtJudgment> createCcJudgmentListWithRandomData(int size){
        return TestInMemoryCcObjectFactory.createCcJudgmentListWithRandomData(size);
    }




}
