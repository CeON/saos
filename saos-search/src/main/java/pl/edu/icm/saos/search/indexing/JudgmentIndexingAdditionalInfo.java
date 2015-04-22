package pl.edu.icm.saos.search.indexing;

import com.google.common.base.Objects;

/**
 * @author madryk
 */
public class JudgmentIndexingAdditionalInfo {

    private long judgmentId;
    
    private long referencingCount;

    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JudgmentIndexingAdditionalInfo(long judgmentId, long referencingCount) {
        this.judgmentId = judgmentId;
        this.referencingCount = referencingCount;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public long getJudgmentId() {
        return judgmentId;
    }

    public long getReferencingCount() {
        return referencingCount;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setJudgmentId(long judgmentId) {
        this.judgmentId = judgmentId;
    }

    public void setReferencingCount(long referencingCount) {
        this.referencingCount = referencingCount;
    }

    
    //------------------------ hashCode & equals --------------------------

    @Override
    public int hashCode() {
        return Objects.hashCode(judgmentId, referencingCount);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final JudgmentIndexingAdditionalInfo other = (JudgmentIndexingAdditionalInfo) obj;
        return Objects.equal(this.judgmentId, other.judgmentId) &&
                Objects.equal(this.referencingCount, other.referencingCount);
    }
}
