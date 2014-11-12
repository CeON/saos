package pl.edu.icm.saos.importer.common;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentWithCorrectionList<T extends Judgment> {
    
    
    private T judgment;
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    /*public JudgmentWithCorrectionList(J judgment) {
        super();
        this.judgment = judgment;
    }*/
    
    public JudgmentWithCorrectionList(T judgment, ImportCorrectionList correctionList) {
        super();
        this.judgment = judgment;
        this.correctionList = correctionList;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public T getJudgment() {
        return judgment;
    }
    
    /** Corrections made to the source judgment during import */
    public ImportCorrectionList getCorrectionList() {
        return correctionList;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setJudgment(T judgment) {
        this.judgment = judgment;
    }
    
    public void setCorrectionList(ImportCorrectionList correctionList) {
        this.correctionList = correctionList;
    }

    
    
}
