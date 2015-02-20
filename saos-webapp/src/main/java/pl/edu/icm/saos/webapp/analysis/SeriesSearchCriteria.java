package pl.edu.icm.saos.webapp.analysis;

/**
 * Search criteria for a single series
 * 
 * @author Łukasz Dumiszewski
 */

public class SeriesSearchCriteria {

    
    private String phrase;

    
    //------------------------ GETTERS --------------------------
    
    public String getPhrase() {
        return phrase;
    }

    //------------------------ SETTERS --------------------------
    
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
    
}
