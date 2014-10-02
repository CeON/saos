package pl.edu.icm.saos.persistence.model.importer;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Judgment raw data received imported from the json files generated locally from supreme common court judgment
 * html 
 * 
 * 
 * @author Łukasz Dumiszewski
 */
@Table(schema="importer")
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_simple_raw_source_sc_judgment", allocationSize = 1, sequenceName = "seq_simple_raw_source_sc_judgment")
public class SimpleRawSourceScJudgment extends RawSourceJudgment {
    
    
    
    private String jsonContent;
    
    
    
      
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_simple_raw_source_sc_judgment")
    @Override
    public int getId() {
        return id;
    }

    
    public String getJsonContent() {
        return jsonContent;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    
    
    //------------------------ HashCode & Equals --------------------------
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((jsonContent == null) ? 0 : jsonContent.hashCode());
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
        SimpleRawSourceScJudgment other = (SimpleRawSourceScJudgment) obj;
        if (jsonContent == null) {
            if (other.jsonContent != null)
                return false;
        } else if (!jsonContent.equals(other.jsonContent))
            return false;
        return true;
    }


   

    //------------------------ toString --------------------------
   
    @Override
    public String toString() {
        return "SimpleRawSourceScJudgment [jsonContent=" + jsonContent
                + ", id=" + id + ", isProcessed()=" + isProcessed()
                + ", getProcessingDate()=" + getProcessingDate() + "]";
    }
    
}
