package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.joda.time.LocalDate;

import pl.edu.icm.saos.importer.notapi.common.LocalDateIsoDeserializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Lists;

/**
 * Representation of json constitutional tribunal judgment
 * 
 * @author madryk
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceCtJudgment {

    private String caseNumber;
    private LocalDate judgmentDate;
    private String judgmentType;
    private String textContent;
    private List<SourceCtJudge> judges = Lists.newArrayList();
    private List<String> courtReporters = Lists.newArrayList();
    private List<SourceCtDissentingOpinion> dissentingOpinions = Lists.newArrayList();
    private Source source;
    
    
    //------------------------ GETTERS --------------------------
    
    @NotNull
    public String getCaseNumber() {
        return caseNumber;
    }
    
    @JsonDeserialize(using = LocalDateIsoDeserializer.class)
    public LocalDate getJudgmentDate() {
        return judgmentDate;
    }
    
    public String getJudgmentType() {
        return judgmentType;
    }
    
    public String getTextContent() {
        return textContent;
    }
    
    public List<SourceCtJudge> getJudges() {
        return judges;
    }
    
    public List<String> getCourtReporters() {
        return courtReporters;
    }
    
    public List<SourceCtDissentingOpinion> getDissentingOpinions() {
        return dissentingOpinions;
    }
    
    @NotNull
    public Source getSource() {
        return source;
    }
    
    
    //------------------------ SETTERS --------------------------

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setJudgmentDate(LocalDate judgmentDate) {
        this.judgmentDate = judgmentDate;
    }

    public void setJudgmentType(String judgmentType) {
        this.judgmentType = judgmentType;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setJudges(List<SourceCtJudge> judges) {
        this.judges = judges;
    }

    public void setCourtReporters(List<String> courtReporters) {
        this.courtReporters = courtReporters;
    }

    public void setDissentingOpinions(
            List<SourceCtDissentingOpinion> dissentingOpinions) {
        this.dissentingOpinions = dissentingOpinions;
    }

    public void setSource(Source source) {
        this.source = source;
    }




    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SourceCtJudge {
        
        private String name;
        private List<String> specialRoles = Lists.newArrayList();
        
        
        //------------------------ GETTERS --------------------------
        
        @NotNull
        public String getName() {
            return name;
        }
        
        public List<String> getSpecialRoles() {
            return specialRoles;
        }
        
        //------------------------ SETTERS --------------------------
        
        public void setName(String name) {
            this.name = name;
        }
        
        public void setSpecialRoles(List<String> specialRoles) {
            this.specialRoles = specialRoles;
        }
        
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




    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Source {
        
        private String sourceJudgmentId;
        private String sourceJudgmentUrl;
        
        
        //------------------------ GETTERS --------------------------
        
        @NotNull
        public String getSourceJudgmentId() {
            return sourceJudgmentId;
        }
        
        @NotNull
        public String getSourceJudgmentUrl() {
            return sourceJudgmentUrl;
        }
        
        
        //------------------------ SETTERS --------------------------
        
        public void setSourceJudgmentId(String sourceJudgmentId) {
            this.sourceJudgmentId = sourceJudgmentId;
        }
        
        public void setSourceJudgmentUrl(String sourceJudgmentUrl) {
            this.sourceJudgmentUrl = sourceJudgmentUrl;
        }
        
    }
    
}
