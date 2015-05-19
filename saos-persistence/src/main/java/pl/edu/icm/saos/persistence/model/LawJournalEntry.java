package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;

import pl.edu.icm.saos.persistence.common.DataObject;

import com.google.common.base.Preconditions;

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

    public final static String ENTRY_CODE_PARTS_SEPARATOR = "/";
    
    private int year;
    private int journalNo;
    private int entry;
    private String title;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    /**
     * @throws IllegalArgumentException if title is blank 
     */
    public LawJournalEntry(int year, int journalNo, int entry, String title) {
        super();
        Preconditions.checkArgument(StringUtils.isNotBlank(title));
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
    public long getId() {
        return id;
    }

    
    @Column(nullable=false)
    public int getYear() {
        return year;
    }

    @Column(nullable=false)
    public int getJournalNo() {
        return journalNo;
    }

    @Column(nullable=false)
    public int getEntry() {
        return entry;
    }

    @Column(nullable=false)
    public String getTitle() {
        return title;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Transient
    public String getEntryCode() {
        return getYear() + ENTRY_CODE_PARTS_SEPARATOR + getJournalNo() + ENTRY_CODE_PARTS_SEPARATOR + getEntry();
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

    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "LawJournalEntry [year=" + year + ", journalNo=" + journalNo
                + ", entry=" + entry + ", title=" + title + "]";
    }
    
}
