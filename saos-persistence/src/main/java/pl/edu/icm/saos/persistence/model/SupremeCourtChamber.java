package pl.edu.icm.saos.persistence.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * 
 * pl. izba sądu najwyższego
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_supreme_court_chamber", allocationSize = 1, sequenceName = "seq_supreme_court_chamber")
public class SupremeCourtChamber extends DataObject {

    private String name;
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

    @Column(unique=true, nullable=false)
    public String getName() {
        return name;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setDivisions(List<SupremeCourtChamberDivision> divisions) {
        this.divisions = divisions;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        SupremeCourtChamber other = (SupremeCourtChamber) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
