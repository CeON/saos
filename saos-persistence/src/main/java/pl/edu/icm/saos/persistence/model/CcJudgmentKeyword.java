package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * pl. Hasła tematyczne/ słowa kluczowe
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_cc_judgment_keyword", allocationSize = 1, sequenceName = "seq_cc_judgment_keyword")
public class CcJudgmentKeyword extends DataObject {

    private String phrase;
    private CcJudgmentKeyword parent;

    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cc_judgment_keyword")
    @Override
    public int getId() {
        return id;
    }
    
    public String getPhrase() {
        return phrase;
    }

    @ManyToOne
    public CcJudgmentKeyword getParent() {
        return parent;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public void setParent(CcJudgmentKeyword parent) {
        this.parent = parent;
    }
}
