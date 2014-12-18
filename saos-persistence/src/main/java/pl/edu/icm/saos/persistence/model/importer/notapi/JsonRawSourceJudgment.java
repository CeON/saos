package pl.edu.icm.saos.persistence.model.importer.notapi;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;

/**
 * Superclass for all raw judgments based on json
 * 
 * @author madryk
 */
@MappedSuperclass
public class JsonRawSourceJudgment extends RawSourceJudgment {

    private String jsonContent;

    
    //------------------------ GETTERS --------------------------

    @Column(nullable=false)
    public String getJsonContent() {
        return jsonContent;
    }
    
    
    //------------------------ SETTERS --------------------------

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }
}
