package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json;

import java.util.List;

import javax.validation.constraints.NotNull;

import pl.edu.icm.saos.importer.notapi.common.SourceJudgment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

/**
 * Representation of json constitutional tribunal judgment
 * 
 * @author madryk
 */
public class SourceCtJudgment extends SourceJudgment {

    private String caseNumber;
    private String judgmentType;
    
    private List<SourceCtDissentingOpinion> dissentingOpinions = Lists.newArrayList();
    
    
    //------------------------ GETTERS --------------------------
    
    @NotNull
    public String getCaseNumber() {
        return caseNumber;
    }
    
    public String getJudgmentType() {
        return judgmentType;
    }
    
    
    public List<SourceCtDissentingOpinion> getDissentingOpinions() {
        return dissentingOpinions;
    }
    
    
    //------------------------ SETTERS --------------------------

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setJudgmentType(String judgmentType) {
        this.judgmentType = judgmentType;
    }

    public void setDissentingOpinions(
            List<SourceCtDissentingOpinion> dissentingOpinions) {
        this.dissentingOpinions = dissentingOpinions;
    }




    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SourceCtDissentingOpinion {
        
        private String textContent;
        private List<String> authors = Lists.newArrayList();
        
        
        //------------------------ GETTERS --------------------------
        
        public String getTextContent() {
            return textContent;
        }
        
        public List<String> getAuthors() {
            return authors;
        }
        
        
        //------------------------ SETTERS --------------------------
        
        public void setTextContent(String textContent) {
            this.textContent = textContent;
        }
        
        public void setAuthors(List<String> authors) {
            this.authors = authors;
        }
        
    }


    
}
