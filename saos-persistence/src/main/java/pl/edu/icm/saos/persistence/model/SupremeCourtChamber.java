package pl.edu.icm.saos.persistence.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * 
 * pl. izba sądu najwyższego
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
@SequenceGenerator(name = "seq_supreme_court_chamber", allocationSize = 1, sequenceName = "seq_supreme_court_chamber")
public class SupremeCourtChamber extends DataObject {

    private List<SupremeCourtChamberDivision> divisions;

    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_supreme_court_chamber")
    @Override
    public int getId() {
        return id;
    }
    
    @OneToMany(cascade=CascadeType.ALL, mappedBy="supremeCourtChamber")
    public List<SupremeCourtChamberDivision> getDivisions() {
        return divisions;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setDivisions(List<SupremeCourtChamberDivision> divisions) {
        this.divisions = divisions;
    }
}
