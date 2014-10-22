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
import javax.persistence.Transient;

import pl.edu.icm.saos.persistence.common.DataObject;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

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
    private List<SupremeCourtChamberDivision> divisions = Lists.newArrayList();

    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_supreme_court_chamber")
    @Override
    public int getId() {
        return id;
    }
    
    @OneToMany(cascade=CascadeType.ALL, mappedBy="scChamber")
    private List<SupremeCourtChamberDivision> getDivisions_() {
        return divisions;
    }

    @Transient
    public List<SupremeCourtChamberDivision> getDivisions() {
        return ImmutableList.copyOf(getDivisions_());
    }
    
    @Column(unique=true, nullable=false)
    public String getName() {
        return name;
    }

    
    //------------------------ LOGIC --------------------------
    
    public void addDivision(SupremeCourtChamberDivision division) {
        division.setScChamber(this);
        divisions.add(division);
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @SuppressWarnings("unused") // for hibernate
    private void setDivisions_(List<SupremeCourtChamberDivision> divisions) {
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

    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "SupremeCourtChamber [name=" + name + ", id=" + id + "]";
    }
}
