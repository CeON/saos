package pl.edu.icm.saos.persistence.common;

import pl.edu.icm.saos.persistence.model.DeletedJudgment;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.SourceCode;

/**
* @author Łukasz Dumiszewski
*/

public final class TestInMemoryDeletedJudgmentFactory {

    
    //------------------------ CONSTRUCTORS --------------------------
    
    private TestInMemoryDeletedJudgmentFactory() {
        throw new IllegalStateException("may not be instantiated");
    }
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates a deleted judgment object for the given judgment id
     */
    public static DeletedJudgment createDeletedJudgment(long judgmentId) {
        
        DeletedJudgment deletedJudgment = new DeletedJudgment();
        deletedJudgment.setJudgmentId(judgmentId);
        
        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setSourceCode(SourceCode.COMMON_COURT);
        sourceInfo.setSourceJudgmentId(judgmentId + "SRC");
        deletedJudgment.setSourceInfo(sourceInfo);
        
        return deletedJudgment;
    }
    
}
