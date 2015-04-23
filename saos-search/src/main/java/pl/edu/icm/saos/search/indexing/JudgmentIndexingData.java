package pl.edu.icm.saos.search.indexing;

import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * Class representing data needed for judgment indexing process.
 * 
 * @author madryk
 */
public class JudgmentIndexingData {

    private Judgment judgment;
    
    private long referencingCount;
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * Returns {@link Judgment} to index
     */
    public Judgment getJudgment() {
        return judgment;
    }
    
    /**
     * Returns number of judgments referencing to {@link Judgment} returned by {@link #getJudgment()}
     */
    public long getReferencingCount() {
        return referencingCount;
    }
    
    //------------------------ SETTERS --------------------------
    
    public void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }
    
    public void setReferencingCount(long referencingCount) {
        this.referencingCount = referencingCount;
    }
    
}
