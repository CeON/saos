package pl.edu.icm.saos.persistence.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import pl.edu.icm.saos.common.visitor.Visitor;
import pl.edu.icm.saos.persistence.common.DataObject;

import javax.persistence.*;
import java.util.List;

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

    @Override
    public void passVisitorDown(Visitor visitor) {
        getDivisions_().stream().forEach(d->d.accept(visitor));
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
