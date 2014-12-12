package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.importer.notapi.common.DateTimeDeserializer;
import pl.edu.icm.saos.importer.notapi.common.LocalDateIsoDeserializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Lists;

/**
 * Representation of json supreme court judgment 
 * 
 * @author Łukasz Dumiszewski
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceScJudgment {
    
    private String textContent;
    private Source source;
    private String caseNumber;
    private LocalDate judgmentDate;
    private String supremeCourtJudgmentForm;
    private String personnelType;
    private List<String> supremeCourtChambers = Lists.newArrayList();
    private String supremeCourtChamberDivision;
    private List<SourceScJudge> judges = Lists.newArrayList();
    
    //------------------------ GETTERS --------------------------

    public String getTextContent() {
        return textContent;
    }

    @NotNull
    public Source getSource() {
        return source;
    }

    @NotNull
    public String getCaseNumber() {
        return caseNumber;
    }
    
    @JsonDeserialize(using = LocalDateIsoDeserializer.class)
    public LocalDate getJudgmentDate() {
        return judgmentDate;
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

    @Valid
    public List<SourceScJudge> getJudges() {
        return judges;
    }




    
    //------------------------ SETTERS --------------------------
    
    public void setSource(Source source) {
        this.source = source;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
    
    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setJudgmentDate(LocalDate judgmentDate) {
        this.judgmentDate = judgmentDate;
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

    public void setJudges(List<SourceScJudge> judges) {
        this.judges = judges;
    }


 



    
    
    
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Source {
        
        private String sourceJudmentId;
        private String sourceJudgmentUrl;
        private DateTime publicationDateTime;
        
        
        //------------------------ GETTERS --------------------------
        
        @NotNull
        public String getSourceJudgmentId() {
            return sourceJudmentId;
        }
        
        @NotNull
        public String getSourceJudgmentUrl() {
            return sourceJudgmentUrl;
        }
        
        @JsonDeserialize(using = DateTimeDeserializer.class)
        public DateTime getPublicationDateTime() {
            return publicationDateTime;
        }
        
        
        //------------------------ SETTERS --------------------------
        
        public void setSourceJudgmentId(String sourceJudgmentId) {
            this.sourceJudmentId = sourceJudgmentId;
        }
        public void setSourceJudgmentUrl(String sourceJudgmentUrl) {
            this.sourceJudgmentUrl = sourceJudgmentUrl;
        }
        public void setPublicationDateTime(DateTime publicationDateTime) {
            this.publicationDateTime = publicationDateTime;
        }
       
    }





    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SourceScJudge {
        
        private String name;
        private String function;
        private List<String> specialRoles = Lists.newArrayList();
        
        
        //------------------------ GETTERS --------------------------
        
        @NotNull
        public String getName() {
            return name;
        }
        public String getFunction() {
            return function;
        }
        public List<String> getSpecialRoles() {
            return specialRoles;
        }
        
        
        //------------------------ SETTERS --------------------------
        
        public void setName(String name) {
            this.name = name;
        }
        public void setFunction(String function) {
            this.function = function;
        }
        public void setSpecialRoles(List<String> specialRoles) {
            this.specialRoles = specialRoles;
        }
        
        
        
        
    }




 

    
}
