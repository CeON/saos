package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * 
 * pl. pozycja dziennika ustaw
 * 
 * @author Łukasz Dumiszewski
 */
@Table(uniqueConstraints={@UniqueConstraint(name="law_journal_entry_unique", columnNames={"year", "journalNo", "entry"})})
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_law_journal_entry", allocationSize = 1, sequenceName = "seq_law_journal_entry")
public class LawJournalEntry extends DataObject {

    
    private int year;
    private int journalNo;
    private int entry;
    private String title;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public LawJournalEntry(int year, int journalNo, int entry, String title) {
        super();
        this.year = year;
        this.journalNo = journalNo;
        this.entry = entry;
        this.title = title;
    }
    
    public LawJournalEntry() {
        
    }


    
    
    
    
    //------------------------ GETTERS --------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_law_journal_entry")
    @Override
    public int getId() {
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
        LawJournalEntry other = (LawJournalEntry) obj;
        if (entry != other.entry)
            return false;
        if (journalNo != other.journalNo)
            return false;
        if (year != other.year)
            return false;
        return true;
    }
    
}
