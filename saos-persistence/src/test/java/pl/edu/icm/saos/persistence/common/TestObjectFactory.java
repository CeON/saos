package pl.edu.icm.saos.persistence.common;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.model.*;

import javax.transaction.Transactional;
import java.util.List;

import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.*;

/**
 * Provides factory methods for creating domain objects with default values
 * which can be used in tests.
 * @author pavtel
 */
@Service
public class TestObjectFactory {

    @Autowired
    private TestScObjectFactory scObjectFactory;

    @Autowired
    private TestCcObjectFactory ccObjectFactory;


    //------------------------ LOGIC --------------------------

    /**
     * Creates {@link TestObjectContext} hierarchy with default field data.
     * @return TestObjectContext.
     */
    @Transactional
    public TestObjectContext createTestObjectContext(boolean save){
        CommonCourtJudgment ccJudgment = ccObjectFactory.createCcJudgment(save);
        SupremeCourtJudgment scJudgment = scObjectFactory.createScJudgment(save);

        TestObjectContext testObjectContext = new TestObjectContext();
        testObjectContext.setCcJudgment(ccJudgment);
        testObjectContext.setScJudgment(scJudgment);

        return testObjectContext;
    }

    /**
     * Creates {@link CommonCourtJudgment} hierarchy with default field data.
     * @return CommonCourtJudgment.
     */
    @Transactional
    public CommonCourtJudgment createCcJudgment(boolean save){
        return ccObjectFactory.createCcJudgment(save);
    }

    /**
     * Creates {@link SupremeCourtJudgment} hierarchy with default field data.
     * @return SupremeCourtJudgment.
     */
    @Transactional
    public SupremeCourtJudgment createScJudgment(boolean save){
        return scObjectFactory.createScJudgment(save);
    }

    /**
     * Creates {@link SupremeCourtChamber} hierarchy with default field data.
     * @return SupremeCourtJudgment.
     */
    @Transactional
    public SupremeCourtChamber createScChamber(boolean save){
        return scObjectFactory.createScChamber(save);
    }

    /**
     * Creates {@link CommonCourt} hierarchy with default field data.
     * @return CommonCourt
     */
    @Transactional
    public CommonCourt createCcCourt(boolean save){
        return ccObjectFactory.createCcCourt(save);
    }

    /**
     * Creates {@link JudgmentSourceInfo} with default field data.
     * @return JudgmentSourceInfo.
     */
    public JudgmentSourceInfo createJudgmentSourceInfo(){
        JudgmentSourceInfo judgmentSourceInfo = new JudgmentSourceInfo();

        judgmentSourceInfo.setSourceCode(SOURCE_CODE);
        judgmentSourceInfo.setSourceJudgmentId(SOURCE_JUDGMENT_ID);
        judgmentSourceInfo.setSourceJudgmentUrl(SOURCE_JUDGMENT_URL);
        judgmentSourceInfo.setPublisher(SOURCE_PUBLISHER);
        judgmentSourceInfo.setReviser(SOURCE_REVISER);
        judgmentSourceInfo.setPublicationDate(new DateTime(SOURCE_PUBLICATION_DATE_IN_MILLISECONDS, DateTimeZone.UTC));

        return judgmentSourceInfo;
    }



    /**
     * Creates {@link SupremeCourtJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return SupremeCourtJudgment
     */
    @Transactional
    public SupremeCourtJudgment createSimpleScJudgment(boolean save){
        return scObjectFactory.createSimpleScJudgment(save);
    }

    /**
     * Creates {@link CommonCourtJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return CommonCourtJudgment
     */
    @Transactional
    public CommonCourtJudgment createSimpleCcJudgment(boolean save){
        return ccObjectFactory.createSimpleCcJudgment(save);
    }

    /**
     * Creates list of {@link CommonCourtJudgment} with fields filled with random values.
     * List's size is 10.
     * @return CommonCourtJudgment
     */
    @Transactional
    public List<CommonCourtJudgment> createCcJudgmentListWithRandomData(boolean save){
        List<CommonCourtJudgment> judgments = createCcJudgmentListWithRandomData(10, save);
        return judgments;

    }

    /**
     * Creates list of {@link CommonCourtJudgment} with fields filled with random values.
     * @param size of the list.
     * @return CommonCourtJudgment
     */
    @Transactional
    public List<CommonCourtJudgment> createCcJudgmentListWithRandomData(int size, boolean save){
        return ccObjectFactory.createCcJudgmentListWithRandomData(size, save);
    }



    /**
     * Creates list of {@link CcJudgmentKeyword} with random data and given size.
     * @param size of the list.
     * @return keywords list.
     */
    public List<CcJudgmentKeyword> createCcKeywordListWith(int size, boolean save){
        return ccObjectFactory.createCcKeywordListWith(size, save);
    }


}
