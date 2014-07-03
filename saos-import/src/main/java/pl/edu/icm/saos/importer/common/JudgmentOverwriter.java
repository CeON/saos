package pl.edu.icm.saos.importer.common;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */

public interface JudgmentOverwriter<T extends Judgment> {

    
    /**
     * Overwrites oldJudgment data with newJudgment data</br>
     * <br/>
     * <ul>Does not overwrite properties related to persistence layer:
     * <li>{@link DataObject#getId()}</li>
     * <li>{@link DataObject#getVer()}</li>
     * <li>{@link DataObject#getCreationDate()}</li>
     * </ul>
     * 
     */
    public void overwriteJudgment(T oldJudgment, T newJudgment);
    
    
}
