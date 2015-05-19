package pl.edu.icm.saos.enrichment.apply.refregulations;

public class ReferencedRegulationsTagValueItem {

    private String journalTitle;
    private int journalNo;
    private int journalYear;
    private int journalEntry;
    private String text;
    
    
    public String getJournalTitle() {
        return journalTitle;
    }
    public int getJournalNo() {
        return journalNo;
    }
    public int getJournalYear() {
        return journalYear;
    }
    public int getJournalEntry() {
        return journalEntry;
    }
    public String getText() {
        return text;
    }
    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }
    public void setJournalNo(int journalNo) {
        this.journalNo = journalNo;
    }
    public void setJournalYear(int journalYear) {
        this.journalYear = journalYear;
    }
    public void setJournalEntry(int journalEntry) {
        this.journalEntry = journalEntry;
    }
    public void setText(String text) {
        this.text = text;
    }
    
    
    
}
