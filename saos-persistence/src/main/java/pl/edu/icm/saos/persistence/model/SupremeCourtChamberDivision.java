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
 * pl. wydział izby sądu najwyższego 
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
@SequenceGenerator(name = "seq_supreme_court_chamber_div", allocationSize = 1, sequenceName = "seq_supreme_court_chamber_div")
public class SupremeCourtChamberDivision extends DataObject {

    private SupremeCourtChamber supremeCourtChamber;

    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_supreme_court_chamber_div")
    @Override
    public int getId() {
        return id;
    }
    
    @ManyToOne
    public SupremeCourtChamber getSupremeCourtChamber() {
        return supremeCourtChamber;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setSupremeCourtChamber(SupremeCourtChamber supremeCourtChamber) {
        this.supremeCourtChamber = supremeCourtChamber;
    }
    
    
    
    
}
