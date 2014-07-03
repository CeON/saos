package pl.edu.icm.saos.importer.common;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.model.Judgment;

import com.google.common.base.Preconditions;

/**
 * Simplifies writing of overwriters of {@link Judgment} </br>
 * Implementation classes will only need to overwrite specific properties of the given subclass of {@link Judgment} 
 * 
 * 
 * @author Łukasz Dumiszewski
 */

public abstract class AbstractJudgmentOverwriter<T extends Judgment> implements JudgmentOverwriter<T> {

    
    private JudgmentOverwriter<Judgment> judgmentCommonDataOverwriter;
    
    
    public final void overwriteJudgment(T oldJudgment, T newJudgment) {
        Preconditions.checkNotNull(oldJudgment);
        Preconditions.checkNotNull(newJudgment);
        
        judgmentCommonDataOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        overwriteSpecificData(oldJudgment, newJudgment);
    }



       
    protected abstract void overwriteSpecificData(T oldJudgment, T newJudgment);



    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setJudgmentOverwriter(JudgmentOverwriter<Judgment> judgmentCommonDataOverwriter) {
        this.judgmentCommonDataOverwriter = judgmentCommonDataOverwriter;
    }

    

    
    

    
}
