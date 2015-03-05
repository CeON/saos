package pl.edu.icm.saos.search.analysis.request;

import java.util.Objects;

/**
 * Judgment search criteria used for generating data series
 * 
 * @author Łukasz Dumiszewski
 */

public class JudgmentSeriesCriteria {

    
    private String phrase;

    
    //------------------------ GETTERS --------------------------
    
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
        return Objects.hash(this.phrase);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final JudgmentSeriesCriteria other = (JudgmentSeriesCriteria) obj;
        
        return Objects.equals(this.phrase, other.phrase);

    }
    
    
}
