package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="judgment_rawText_journalEntry_unique", columnNames={"fk_judgment", "rawText", "fk_law_journal_entry"})})
@Cacheable(true)
@SequenceGenerator(name = "seq_referenced_regulation", allocationSize = 1, sequenceName = "seq_referenced_regulation")
public class ReferencedRegulation extends DataObject {

    
    private Judgment judgment;
    private String rawText;
    private LawJournalEntry lawJournalEntry;
    
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_referenced_regulation")
    @Override
    public int getId() {
        return id;
    }

    @ManyToOne
    public Judgment getJudgment() {
        return judgment;
    }

    @ManyToOne
    public LawJournalEntry getLawJournalEntry() {
        return lawJournalEntry;
    }

    /**
     *  The raw legal basis text from the judgment source. <br/>
     * */
    public String getRawText() {
        return rawText;
    }

    

    //------------------------ SETTERS --------------------------
    
    public void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }


    public void setLawJournalEntry(LawJournalEntry lawJournalEntry) {
        this.lawJournalEntry = lawJournalEntry;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }
    
    
   
    
}
