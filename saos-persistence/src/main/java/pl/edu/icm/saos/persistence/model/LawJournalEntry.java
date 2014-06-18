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
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_law_journal_entry")
    @Override
    public int getId() {
        return id;
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
