package pl.edu.icm.saos.importer.common;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.model.Judgment;

import com.google.common.base.Preconditions;

/**
 * Implementation of {@link JudgmentOverwriter} that delegates the actual overwriting of {@link Judgment}s to
 * the {@link #setCommonJudgmentOverwriter(JudgmentOverwriter)} and {@link #setSpecificJudgmentOverwriter(JudgmentOverwriter)}
 *  
 * 
 * @author Łukasz Dumiszewski
 */

public class DelegatingJudgmentOverwriter<T extends Judgment> implements JudgmentOverwriter<T> {

    
    private JudgmentOverwriter<Judgment> commonJudgmentOverwriter;
    
    private JudgmentOverwriter<T> specificJudgmentOverwriter;
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public final void overwriteJudgment(T oldJudgment, T newJudgment) {
        Preconditions.checkNotNull(oldJudgment);
        Preconditions.checkNotNull(newJudgment);
        
        commonJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        specificJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
    }


    //------------------------ SETTERS --------------------------
    
    /**
     * Ovewriter of data accessible from {@link Judgment} class 
     */
    @Autowired
    public void setCommonJudgmentOverwriter(JudgmentOverwriter<Judgment> commonJudgmentOverwriter) {
        this.commonJudgmentOverwriter = commonJudgmentOverwriter;
    }

    /**
     * Overwriter of judgment data specific to the given {@link Judgment} implementation
     */
    public void setSpecificJudgmentOverwriter(JudgmentOverwriter<T> specificJudgmentOverwriter) {
        this.specificJudgmentOverwriter = specificJudgmentOverwriter;
    }


    
    

    
}
