package pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.edu.icm.saos.importer.notapi.common.SourceJudgment;

/**
 * Representation of json national appeal chamber judgment
 * 
 * @author madryk
 */
public class SourceNacJudgment extends SourceJudgment {

    private String judgmentType;
    
    private List<String> caseNumbers;

    
    //------------------------ GETTERS --------------------------

    public String getJudgmentType() {
        return judgmentType;
    }

    @JsonProperty("caseNumber")
    public List<String> getCaseNumbers() {
        return caseNumbers;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setJudgmentType(String judgmentType) {
        this.judgmentType = judgmentType;
    }

    public void setCaseNumbers(List<String> caseNumbers) {
        this.caseNumbers = caseNumbers;
    }
    
}
