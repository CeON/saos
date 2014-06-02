package pl.edu.icm.saos.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * pl. Wydział Sądu Powszechnego
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
@SequenceGenerator(name = "seq_common_court_division", allocationSize = 1, sequenceName = "seq_common_court_division")
public class CommonCourtDivision extends DataObject {

    private CommonCourt court;
    private String name;

    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_common_court_division")
    @Override
    public int getId() {
        return id;
    }
    
    
    @ManyToOne
    public CommonCourt getCourt() {
        return court;
    }

    public String getName() {
        return name;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }

    public void setCourt(CommonCourt court) {
        this.court = court;
    }
    
}
