package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json;

import java.util.List;

import javax.validation.constraints.NotNull;

import pl.edu.icm.saos.importer.notapi.common.SourceJudgment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

/**
 * Representation of json supreme court judgment 
 * 
 * @author Łukasz Dumiszewski
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceScJudgment extends SourceJudgment {
    
    private String caseNumber;
    private String supremeCourtJudgmentForm;
    private String personnelType;
    private List<String> supremeCourtChambers = Lists.newArrayList();
    private String supremeCourtChamberDivision;
    
    
    //------------------------ GETTERS --------------------------

    @NotNull
    public String getCaseNumber() {
        return caseNumber;
    }

    public String getSupremeCourtJudgmentForm() {
        return supremeCourtJudgmentForm;
    }

    public String getPersonnelType() {
        return personnelType;
    }

    public List<String> getSupremeCourtChambers() {
        return supremeCourtChambers;
    }

    public String getSupremeCourtChamberDivision() {
        return supremeCourtChamberDivision;
    }






    
    //------------------------ SETTERS --------------------------
    
    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setSupremeCourtJudgmentForm(String supremeCourtJudgmentForm) {
        this.supremeCourtJudgmentForm = supremeCourtJudgmentForm;
    }

    public void setPersonnelType(String personnelType) {
        this.personnelType = personnelType;
    }

    public void setSupremeCourtChambers(List<String> supremeCourtChambers) {
        this.supremeCourtChambers = supremeCourtChambers;
    }

    public void setSupremeCourtChamberDivision(String supremeCourtChamberDivision) {
        this.supremeCourtChamberDivision = supremeCourtChamberDivision;
    }


}
