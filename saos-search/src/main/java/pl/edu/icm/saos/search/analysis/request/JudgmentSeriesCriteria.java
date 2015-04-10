package pl.edu.icm.saos.search.analysis.request;

import java.util.Objects;

import org.joda.time.LocalDate;

/**
 * Judgment search criteria used for generating data series
 * 
 * @author Łukasz Dumiszewski
 */

public class JudgmentSeriesCriteria {

    
    private String phrase;
    
    private LocalDate startJudgmentDate;
    
    private LocalDate endJudgmentDate;

    
    //------------------------ GETTERS --------------------------
    
    public String getPhrase() {
        return phrase;
    }
    
    public LocalDate getStartJudgmentDate() {
        return startJudgmentDate;
    }

    public LocalDate getEndJudgmentDate() {
        return endJudgmentDate;
    }

        
    //------------------------ SETTERS --------------------------
    
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
    
    public void setStartJudgmentDate(LocalDate startJudgmentDate) {
        this.startJudgmentDate = startJudgmentDate;
    }

    public void setEndJudgmentDate(LocalDate endJudgmentDate) {
        this.endJudgmentDate = endJudgmentDate;
    }
    

    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        return Objects.hash(this.phrase, this.startJudgmentDate, this.endJudgmentDate);
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
        
        return Objects.equals(this.phrase, other.phrase) &&
               Objects.equals(this.startJudgmentDate, other.endJudgmentDate) &&
               Objects.equals(this.endJudgmentDate, other.endJudgmentDate);

    }

   
}
