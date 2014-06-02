package pl.edu.icm.saos.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * pl. Sąd administracyjny
 * <br/> <br/>
 * Dictionary of administrative courts
 * 
 * @author Łukasz Dumiszewski
 */

@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
@SequenceGenerator(name = "seq_adm_court", allocationSize = 1, sequenceName = "seq_adm_court")
public class AdministrativeCourt extends DataObject {
    
    private String name;

    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_adm_court")
    @Override
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }
    
}
