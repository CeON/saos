package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * pl. Hasła tematyczne/ słowa kluczowe
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="phrase_unique", columnNames="phrase")})
@Cacheable(true)
@SequenceGenerator(name = "seq_cc_judgment_keyword", allocationSize = 1, sequenceName = "seq_cc_judgment_keyword")
public class CcJudgmentKeyword extends DataObject {

   

    private String phrase;
   
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public CcJudgmentKeyword() {
        
    }

    public CcJudgmentKeyword(String phrase) {
        this.phrase = phrase;
    }
    
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

    
    
    //------------------------ SETTERS --------------------------
    
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((phrase == null) ? 0 : phrase.hashCode());
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
        CcJudgmentKeyword other = (CcJudgmentKeyword) obj;
        if (phrase == null) {
            if (other.phrase != null)
                return false;
        } else if (!phrase.equals(other.phrase))
            return false;
        return true;
    }

    
}
