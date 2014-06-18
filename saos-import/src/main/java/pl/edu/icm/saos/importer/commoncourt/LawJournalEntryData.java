package pl.edu.icm.saos.importer.commoncourt;

/**
 * @author Łukasz Dumiszewski
 */

public class LawJournalEntryData {
    
    
    private int year;
    private int journalNo;
    private int entry;
    private String title;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public LawJournalEntryData(int year, int journalNo, int entry, String title) {
        super();
        this.year = year;
        this.journalNo = journalNo;
        this.entry = entry;
        this.title = title;
    }
    
    //------------------------ GETTERS --------------------------
    
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
    
    //------------------------ SETTERS --------------------------
    
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
}
