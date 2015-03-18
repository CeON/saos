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
    
    private String judgmentContentFilename;

    
    //------------------------ GETTERS --------------------------

    @Type(type = "StringJsonObject" )
    @Column(nullable=false)
    public String getJsonContent() {
        return jsonContent;
    }
    
    public String getJudgmentContentFilename() {
        return judgmentContentFilename;
    }
    
    
    //------------------------ SETTERS --------------------------

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }
    
    public void setJudgmentContentFilename(String judgmentContentFilename) {
        this.judgmentContentFilename = judgmentContentFilename;
    }
    
    //------------------------ HashCode & Equals --------------------------
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((getJsonContent() == null) ? 0 : getJsonContent().hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JsonRawSourceJudgment other = (JsonRawSourceJudgment) obj;
        if (getJsonContent() == null) {
            if (other.getJsonContent() != null)
                return false;
        } else if (!getJsonContent().equals(other.getJsonContent()))
            return false;
        return true;
    }


   

    //------------------------ toString --------------------------
   
    @Override
    public String toString() {
        return "JsonRawSourceJudgment [jsonContent=" + getJsonContent()
                + ", id=" + id + ", isProcessed()=" + isProcessed()
                + ", getProcessingDate()=" + getProcessingDate() + "]";
    }

}
