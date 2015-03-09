package pl.edu.icm.saos.webapp.analysis.request;

import java.util.Objects;

/**
 * Search criteria for a single series
 * 
 * @author Łukasz Dumiszewski
 */

public class JudgmentSeriesFilter {

    
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
        
        final JudgmentSeriesFilter other = (JudgmentSeriesFilter) obj;
        
        return Objects.equals(this.phrase, other.phrase);

    }


   
    
    
}
