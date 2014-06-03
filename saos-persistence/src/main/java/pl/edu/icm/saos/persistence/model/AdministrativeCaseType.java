package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * pl. typ sprawy administracyjnej
 * @author Łukasz Dumiszewski
 */
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_adm_case_type", allocationSize = 1, sequenceName = "seq_adm_case_type")
public class AdministrativeCaseType extends DataObject {
    
    
    private String code;
    private String description;
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_adm_case_type")
    @Override
    public int getId() {
        return id;
    }
    
    public String getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
    
    //------------------------ SETTERS --------------------------
    
    /** pl. symbol, kod */
    public void setCode(String code) {
        this.code = code;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
