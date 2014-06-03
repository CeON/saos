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
@SequenceGenerator(name = "seq_common_court_judgment_keyword", allocationSize = 1, sequenceName = "seq_common_court_judgment_keyword")
public class CommonCourtJudgmentKeyword extends DataObject {

    private String name;
    private CommonCourtJudgmentKeyword parent;

    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_common_court_judgment_keyword")
    @Override
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    @ManyToOne
    public CommonCourtJudgmentKeyword getParent() {
        return parent;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }

    public void setParent(CommonCourtJudgmentKeyword parent) {
        this.parent = parent;
    }
}
