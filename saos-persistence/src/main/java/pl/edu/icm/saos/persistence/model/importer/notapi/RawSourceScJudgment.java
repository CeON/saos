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
    
    
    


        
}
