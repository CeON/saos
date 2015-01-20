package pl.edu.icm.saos.persistence.model.importer.notapi;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import pl.edu.icm.saos.persistence.common.StringJsonUserType;
import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;

/**
 * Superclass for all raw judgments based on json
 * 
 * @author madryk
 */
@MappedSuperclass
@TypeDefs( {@TypeDef( name= "StringJsonObject", typeClass = StringJsonUserType.class)})
public class JsonRawSourceJudgment extends RawSourceJudgment {

    private String jsonContent;

    
    //------------------------ GETTERS --------------------------

    @Type(type = "StringJsonObject" )
    @Column(nullable=false)
    public String getJsonContent() {
        return jsonContent;
    }
    
    
    //------------------------ SETTERS --------------------------

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }
}
