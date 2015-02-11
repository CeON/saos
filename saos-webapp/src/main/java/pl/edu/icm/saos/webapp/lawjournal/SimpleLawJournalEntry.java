package pl.edu.icm.saos.webapp.lawjournal;

/**
 * @author madryk
 */
public class SimpleLawJournalEntry {

    private long id;
    private int year;
    private int journalNo;
    private int entry;
    private String title;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public SimpleLawJournalEntry() { }
    
    public SimpleLawJournalEntry(long id, int year, int journalNo, int entry, String title) {
        this.id = id;
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
    
    
    //------------------------ hashCode & equals --------------------------

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + entry;
        result = prime * result + (int) (id ^ (id >>> 32));
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
        if (!(obj instanceof SimpleLawJournalEntry))
            return false;
        SimpleLawJournalEntry other = (SimpleLawJournalEntry) obj;
        if (entry != other.entry)
            return false;
        if (id != other.id)
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

    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "SimpleLawJournalEntry [id=" + id + ", year=" + year
                + ", journalNo=" + journalNo + ", entry=" + entry + ", title="
                + title + "]";
    }
    
}
