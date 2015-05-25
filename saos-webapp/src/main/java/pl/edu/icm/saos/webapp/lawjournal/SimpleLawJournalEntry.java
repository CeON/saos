package pl.edu.icm.saos.webapp.lawjournal;

import java.util.Objects;

/**
 * @author madryk
 */
public class SimpleLawJournalEntry {

    private long id;
    private String code;
    private int year;
    private int journalNo;
    private int entry;
    private String title;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public SimpleLawJournalEntry() { }
    
    public SimpleLawJournalEntry(long id, int year, int journalNo, int entry, String title, String code) {
        this.id = id;
        this.code = code;
        this.year = year;
        this.journalNo = journalNo;
        this.entry = entry;
        this.title = title;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public long getId() {
        return id;
    }
    
    public int getYear() {
        return year;
    }
    
    public int getJournalNo() {
        return journalNo;
    }
    
    public int getEntry() {
        return entry;
    }
    
    public String getTitle() {
        return title;
    }
    
    /**
     * Returns code of law journal entry (same as {@link pl.edu.icm.saos.persistence.model.LawJournalEntry#getEntryCode()})
     */
    public String getCode() {
        return code;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setId(long id) {
        this.id = id;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public void setJournalNo(int journalNo) {
        this.journalNo = journalNo;
    }
    
    public void setEntry(int entry) {
        this.entry = entry;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    
    //------------------------ hashCode & equals --------------------------

    @Override
    public int hashCode() {
        return Objects.hash(id, year, journalNo, entry, title, code);
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final SimpleLawJournalEntry other = (SimpleLawJournalEntry) obj;
        
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.year, other.year)
                && Objects.equals(this.journalNo, other.journalNo)
                && Objects.equals(this.entry, other.entry)
                && Objects.equals(this.title, other.title)
                && Objects.equals(this.code, other.code);
    }
    

    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "SimpleLawJournalEntry [id=" + id + ", year=" + year
                + ", journalNo=" + journalNo + ", entry=" + entry + ", title="
                + title + ", code=" + code + "]";
    }
    
}
