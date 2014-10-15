package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * 
 * pl. forma orzeczenia sądu najwyższego
 * <br/> <br/>
 * Dictionary
 * 
 * @author Łukasz Dumiszewski
 */

@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_supreme_court_judgment_form", allocationSize = 1, sequenceName = "seq_supreme_court_judgment_form")
public class SupremeCourtJudgmentForm extends DataObject {

    private String name;
    
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_supreme_court_judgment_form")
    @Override
    public int getId() {
        return id;
    }
    
    @Column(unique=true, nullable=false)
    public String getName() {
        return name;
    }

    //------------------------ SETTERS --------------------------
    
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
        SupremeCourtJudgmentForm other = (SupremeCourtJudgmentForm) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
}
