package pl.edu.icm.saos.importer.commoncourt.judgment.process;

/**
 * A DTO for holding law journal data
 * 
 * @author Łukasz Dumiszewski
 */

class LawJournalEntryData {
    
    
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

    
    //------------------------ HashCode & Equals --------------------------
        
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + entry;
        result = prime * result + journalNo;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + year;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LawJournalEntryData other = (LawJournalEntryData) obj;
        if (entry != other.entry)
            return false;
        if (journalNo != other.journalNo)
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (year != other.year)
            return false;
        return true;
    }
}
