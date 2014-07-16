package pl.edu.icm.saos.persistence.common;

import org.apache.commons.lang3.RandomStringUtils;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public final class TestJudgmentFactory {

    private TestJudgmentFactory() {
        throw new IllegalStateException("may not be instantiated");
    }
    
    
    public static CommonCourtJudgment createCcJudgment() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.setCaseNumber(RandomStringUtils.randomAlphanumeric(10));
        return judgment;
    }
}
