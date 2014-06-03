package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
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
    
    public String getName() {
        return name;
    }

    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }
    
}
