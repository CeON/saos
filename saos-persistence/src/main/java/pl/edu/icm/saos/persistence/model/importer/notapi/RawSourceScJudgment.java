package pl.edu.icm.saos.persistence.model.importer.notapi;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pl.edu.icm.saos.persistence.common.ColumnDefinitionConst;
import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;


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
@SequenceGenerator(name = "seq_notapi_raw_source_sc_judgment", allocationSize = 1, sequenceName = "seq_notapi_raw_source_sc_judgment")
public class RawSourceScJudgment extends RawSourceJudgment {
    
    private boolean multiChambers;
    
    private String jsonContent;
    
    
    
      
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notapi_raw_source_sc_judgment")
    @Override
    public int getId() {
        return id;
    }

    @Column(nullable=false)
    public String getJsonContent() {
        return jsonContent;
    }

    /**
     * Returns true if there are more than one supreme court chamber assigned to this judgment. </br>
     */
    @Column(columnDefinition=ColumnDefinitionConst.BOOLEAN_NOT_NULL_DEFUALT_FALSE)
    public boolean isMultiChambers() {
        return multiChambers;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public void setMultiChambers(boolean multiChambers) {
        this.multiChambers = multiChambers;
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
        RawSourceScJudgment other = (RawSourceScJudgment) obj;
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
