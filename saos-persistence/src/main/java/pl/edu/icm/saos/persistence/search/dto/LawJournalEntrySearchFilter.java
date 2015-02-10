package pl.edu.icm.saos.persistence.search.dto;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author madryk
 */
public class LawJournalEntrySearchFilter extends DatabaseSearchFilter<LawJournalEntry> {

    private static final long serialVersionUID = 1L;
    

    public static class Builder extends DatabaseSearchFilter.Builder<Builder, LawJournalEntrySearchFilter>{

        public Builder() {
            instance = new LawJournalEntrySearchFilter();
        }

        @Override
        protected Builder self() {
            return this;
        }
        
        
        public Builder year(Integer year) {
            instance.setYear(year);
            return this;
        }
        
        public Builder journalNo(Integer journalNo) {
            instance.setJournalNo(journalNo);
            return this;
        }
        
        public Builder entry(Integer entry) {
            instance.setEntry(entry);
            return this;
        }
        
        public Builder text(String text) {
            instance.setText(text);
            return this;
        }
        
        

        @Override
        public LawJournalEntrySearchFilter filter() {
            return instance;
        }
    }

    public static Builder builder(){
        return new Builder();
    }
    
    
    private Integer year;
    private Integer journalNo;
    private Integer entry;
    private String text;
    
    
    //------------------------ GETTERS --------------------------
    
    public Integer getYear() {
        return year;
    }
    
    public Integer getJournalNo() {
        return journalNo;
    }
    
    public Integer getEntry() {
        return entry;
    }
    
    public String getText() {
        return text;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public void setJournalNo(Integer journalNo) {
        this.journalNo = journalNo;
    }
    
    public void setEntry(Integer entry) {
        this.entry = entry;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
}
