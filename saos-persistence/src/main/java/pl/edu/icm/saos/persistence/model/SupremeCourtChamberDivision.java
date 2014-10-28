package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * pl. wydział izby sądu najwyższego 
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="nameChamberUnique", columnNames={"name", "fk_sc_chamber"})})
@Cacheable(true)
@SequenceGenerator(name = "seq_supreme_court_chamber_div", allocationSize = 1, sequenceName = "seq_supreme_court_chamber_div")
public class SupremeCourtChamberDivision extends DataObject {

    
    private String fullName;
    private String name;
    private SupremeCourtChamber scChamber;

    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_supreme_court_chamber_div")
    @Override
    public int getId() {
        return id;
    }
    
    @ManyToOne(optional=false)
    public SupremeCourtChamber getScChamber() {
        return scChamber;
    }

    /**
     * Consists of {@link SupremeCourtChamber#getName()} and {@link #getName()}
     * 
     * e.g. Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych Wydział I
     */
    @Column(unique=true, nullable=false)
    public String getFullName() {
        return fullName;
    }

    /**
     * e.g. Wydział I
     */
    @Column(nullable=false)
    public String getName() {
        return name;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setScChamber(SupremeCourtChamber scChamber) {
        this.scChamber = scChamber;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fullName == null) ? 0 : fullName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime
                * result
                + ((scChamber == null) ? 0 : scChamber
                        .hashCode());
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
        SupremeCourtChamberDivision other = (SupremeCourtChamberDivision) obj;
        if (fullName == null) {
            if (other.fullName != null)
                return false;
        } else if (!fullName.equals(other.fullName))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (scChamber == null) {
            if (other.scChamber != null)
                return false;
        } else if (!scChamber.equals(other.scChamber))
            return false;
        return true;
    }

    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "SupremeCourtChamberDivision [fullName=" + fullName + ", name="
                + name + ", id=" + id + "]";
    }
    
    
    
    
}
