package pl.edu.icm.saos.importer.notapi.common;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Lists;

/**
 * Abstract json representation of judgment
 * 
 * @author madryk
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class SourceJudgment {

    private String textContent;
    private LocalDate judgmentDate;
    private List<SourceJudge> judges = Lists.newArrayList();
    private List<String> courtReporters = Lists.newArrayList();
    private Source source;
    
    
    //------------------------ GETTERS --------------------------

    public String getTextContent() {
        return textContent;
    }
    
    @JsonDeserialize(using = LocalDateIsoDeserializer.class)
    public LocalDate getJudgmentDate() {
        return judgmentDate;
    }
    
    @Valid
    public List<SourceJudge> getJudges() {
        return judges;
    }
    
    public List<String> getCourtReporters() {
        return courtReporters;
    }
    
    @NotNull
    public Source getSource() {
        return source;
    }
    
    
    //------------------------ SETTERS --------------------------

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
    
    public void setJudgmentDate(LocalDate judgmentDate) {
        this.judgmentDate = judgmentDate;
    }
    
    public void setJudges(List<SourceJudge> judges) {
        this.judges = judges;
    }
    
    public void setCourtReporters(List<String> courtReporters) {
        this.courtReporters = courtReporters;
    }
    
    public void setSource(Source source) {
        this.source = source;
    }
    
    
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SourceJudge {
        
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
}
