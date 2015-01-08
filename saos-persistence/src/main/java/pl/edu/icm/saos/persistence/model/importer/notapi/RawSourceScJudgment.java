package pl.edu.icm.saos.persistence.model.importer.notapi;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Judgment raw data received imported from the json files generated locally from supreme common court judgment
 * html 
 * 
 * 
 * @author Łukasz Dumiszewski
 */
@Table(schema="importer", name="notapi_raw_source_sc_judgment")
@Entity
@Cacheable(true)
public class RawSourceScJudgment extends JsonRawSourceJudgment {
    
    
    
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
        RawSourceScJudgment other = (RawSourceScJudgment) obj;
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
        return "SimpleRawSourceScJudgment [jsonContent=" + getJsonContent()
                + ", id=" + id + ", isProcessed()=" + isProcessed()
                + ", getProcessingDate()=" + getProcessingDate() + "]";
    }


        
}
